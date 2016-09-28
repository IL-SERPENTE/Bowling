package net.samagames.bowling.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Pin;
import net.samagames.bowling.entities.StandBall;
import net.samagames.bowling.util.BannersUtil;
import net.samagames.tools.Area;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created by Rigner for project Bowling.
 */
public class BowlingTrack
{
    private Bowling plugin;
    private Location spawn;
    private Area shootArea;
    private Area endArea;
    private Location[] scoreBanners;
    private Location[] ballTrail;
    private Location[] pins;
    private boolean[] shootedPins;
    private Pin[] pinsEntities;

    private BPlayer player;
    private boolean canShoot;

    private BowlingTrack(Bowling plugin, Location spawn, Area shootArea, Area endArea, Location[] scoreBanners, Location[] ballTrail, Location[] pins)
    {
        this.plugin = plugin;
        this.spawn = spawn;
        this.shootArea = shootArea;
        this.endArea = endArea;
        this.scoreBanners = scoreBanners;
        this.ballTrail = ballTrail;
        this.pins = pins;
        this.shootedPins = new boolean[this.pins.length];
        this.pinsEntities = new Pin[this.pins.length];
        Arrays.fill(this.pinsEntities, null);
        this.canShoot = false;

        this.player = null;
    }

    public static BowlingTrack fromJson(Bowling plugin, JsonElement jsonElement)
    {
        try
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Location[] locations = new Location[3];
            locations[0] = LocationUtils.str2loc(jsonObject.get("score-1").getAsString());
            locations[1] = LocationUtils.str2loc(jsonObject.get("score-2").getAsString());
            locations[2] = LocationUtils.str2loc(jsonObject.get("score-3").getAsString());
            Location[] locations2 = new Location[2];
            locations2[0] = LocationUtils.str2loc(jsonObject.get("trail-1").getAsString());
            locations2[1] = LocationUtils.str2loc(jsonObject.get("trail-2").getAsString());
            Location[] locations3 = new Location[10];
            for (int i = 1; i <= 10; i++)
                locations3[i - 1] = LocationUtils.str2loc(jsonObject.get("pin-" + i).getAsString());
            return new BowlingTrack(plugin,
                    LocationUtils.str2loc(jsonObject.get("spawn").getAsString()),
                    Area.str2area(jsonObject.get("shootArea").getAsString()),
                    Area.str2area(jsonObject.get("endArea").getAsString()),
                    locations, locations2, locations3);
        }
        catch (Exception ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Can't load track", ex);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public void setScore(int score)
    {
        if (score > 999 || score < 0)
            throw new IllegalArgumentException("Invalid score " + score + " : must be between 0 and 999");
        for (int i = 2; i >= 0; --i)
        {
            this.scoreBanners[i].getBlock().setType(Material.WALL_BANNER);
            this.scoreBanners[i].getBlock().setData((byte) 5);
            if (this.scoreBanners[i].getBlock().getState() instanceof Banner)
                BannersUtil.editBanner(BannersUtil.getCharBanner((char)('0' + score % 10), DyeColor.BLACK, DyeColor.WHITE), (Banner)this.scoreBanners[i].getBlock().getState());
            score /= 10;
        }
    }

    public void respawn(boolean reset)
    {
        this.canShoot = false;
        new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (this.i == 20)
                {
                    this.cancel();
                    return ;
                }
                for (int i = 0; i < BowlingTrack.this.pinsEntities.length; i++)
                    if (BowlingTrack.this.pinsEntities[i] != null && !BowlingTrack.this.shootedPins[i])
                        BowlingTrack.this.pinsEntities[i].getBukkitEntity().teleport(BowlingTrack.this.pinsEntities[i].getBukkitEntity().getLocation().add(0D, 0.1D, 0D));
                this.i++;
            }
        }.runTaskTimer(this.plugin, 10, 1);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
        {
            if (reset)
                for (int i = 0; i < this.shootedPins.length; i++)
                    this.shootedPins[i] = false;
            for (int i = 0; i < this.pinsEntities.length; i++)
            {
                if (this.pinsEntities[i] != null)
                    this.pinsEntities[i].die();
                this.pinsEntities[i] = this.shootedPins[i] ? null : new Pin(((CraftWorld)this.pins[i].getWorld()).getHandle(), this.pins[i]);
                if (this.pinsEntities[i] != null)
                    ((CraftWorld)this.pins[i].getWorld()).getHandle().addEntity(this.pinsEntities[i]);
            }
        }, 40);

        new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (this.i == 20)
                {
                    this.cancel();
                    BowlingTrack.this.canShoot = true;
                    return ;
                }
                for (int i = 0; i < BowlingTrack.this.pinsEntities.length; i++)
                    if (BowlingTrack.this.pinsEntities[i] != null && !BowlingTrack.this.shootedPins[i])
                        BowlingTrack.this.pinsEntities[i].getBukkitEntity().teleport(BowlingTrack.this.pinsEntities[i].getBukkitEntity().getLocation().add(0D, -0.1D, 0D));
                this.i++;
            }
        }.runTaskTimer(this.plugin, 50, 1);
    }

    public void spawnBall()
    {
        if (this.player == null)
            return ;
        StandBall ball = new StandBall(((CraftWorld)this.ballTrail[0].getWorld()).getHandle(), this.player.getUUID(), this.ballTrail[0], this.player.getBallDescription());
        ball.getWorld().addEntity(ball);
        this.spawnBall(ball);
    }

    public void spawnBall(StandBall ball)
    {
        new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (this.i == 40 || !ball.isAlive())
                {
                    this.cancel();
                    return ;
                }
                ball.setLocation(BowlingTrack.this.ballTrail[0].clone().add(BowlingTrack.this.ballTrail[1].clone().subtract(BowlingTrack.this.ballTrail[0]).multiply((double)i / 40D)));
                this.i++;
            }
        }.runTaskTimer(this.plugin, 10, 1);
    }

    public boolean canShoot()
    {
        return this.canShoot;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public boolean isInShootZone(Location location)
    {
        return this.shootArea.isInArea(location);
    }

    public boolean isInEndZone(Location location)
    {
        return this.endArea.isInArea(location);
    }

    public BPlayer getPlayer()
    {
        return this.player;
    }

    public void setPlayer(BPlayer player)
    {
        this.player = player;
    }

    public void onShootEnd()
    {
        if (this.player == null)
            return ;
        int score = 0;
        for (int i = 0; i < this.pinsEntities.length; i++)
        {
            if (this.pinsEntities[i] == null || this.shootedPins[i])
                continue ;
            Location location1 = this.pinsEntities[i].getBukkitEntity().getLocation();
            Location location2 = this.pins[i].clone();
            location1.setY(0D);
            location2.setY(0D);
            if (location1.distanceSquared(location2) > 0.1D)
            {
                this.shootedPins[i] = true;
                score++;
            }
        }
        this.player.setScore(this.player.getCurrentShoot(), score);

        boolean special = false;

        if (score == 10 && this.player.getCurrentShoot() % 2 == 0)
        {
            this.player.setCurrentShoot(this.player.getCurrentShoot() + 2);
            Titles.sendTitle(this.player.getPlayerIfOnline(), 0, 60, 0, "", ChatColor.GOLD + "★ Strike ★");
            special = true;
        }
        else
        {
            if (this.player.getCurrentShoot() % 2 == 1 && this.player.getScore(this.player.getCurrentShoot() - 1) + score == 10)
            {
                Titles.sendTitle(this.player.getPlayerIfOnline(), 0, 60, 0, "", ChatColor.GOLD + "☆ Spare ☆");
                special = true;
            }
            else
                Titles.sendTitle(this.player.getPlayerIfOnline(), 0, 60, 0, "", ChatColor.YELLOW.toString() + score + " quilles !");
            this.player.setCurrentShoot(this.player.getCurrentShoot() + 1);
        }

        if (this.player.getCurrentShoot() == 20 && !special)
            this.player.setCurrentShoot(this.player.getCurrentShoot() + 1);

        this.setScore(this.player.getTotalScore());

        if (this.player.getCurrentShoot() == 21)
            this.onEnd();
        else
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
            {
                this.respawn(this.player.getCurrentShoot() % 2 == 0);
                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::spawnBall, 50L);
            }, 70L);
    }

    public void onEnd()
    {
        this.canShoot = false;
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
        {
            Titles.sendTitle(this.player.getPlayerIfOnline(), 0, 60, 0, "", ChatColor.YELLOW.toString() + "Score final : " + this.player.getTotalScore());
            this.plugin.getGame().onPlayerEnd(this.player);
        }, 70L);
    }
}
