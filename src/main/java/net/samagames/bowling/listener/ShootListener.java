package net.samagames.bowling.listener;

import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Ball;
import net.samagames.bowling.game.BPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Rigner for project Bowling.
 */
public class ShootListener implements Listener
{
    private Bowling plugin;
    private Map<UUID, Integer> progress;

    public ShootListener(Bowling plugin)
    {
        this.plugin = plugin;
        this.progress = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        BPlayer bPlayer = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
        if (bPlayer != null && bPlayer.getBowlingTrack() != null && bPlayer.getBowlingTrack().canShoot()
                && bPlayer.getBowlingTrack().isInShootZone(event.getPlayer().getLocation()) && event.getItem() != null
                && event.getItem().getType() == Material.WOOL)
        {
            Integer p = this.progress.get(event.getPlayer().getUniqueId());
            if (p == null)
                p = 0;
            p++;
            this.progress.put(event.getPlayer().getUniqueId(), p);

            event.getPlayer().setExp(ShootListener.progressToPercent((float)p - 0.75F));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

            final UUID uuid = event.getPlayer().getUniqueId();
            final int value = p;
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> event.getPlayer().setExp(ShootListener.progressToPercent((float)value - 0.5F)), 1L);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> event.getPlayer().setExp(ShootListener.progressToPercent((float)value - 0.25F)), 2L);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> event.getPlayer().setExp(ShootListener.progressToPercent((float)value)), 3L);

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.checkShoot(uuid, value), 6L);
        }
    }

    private void checkShoot(UUID uuid, int oldValue)
    {
        Integer p = this.progress.get(uuid);
        if (p == null || p != oldValue)
            return ;
        float power = ShootListener.progressToPercent(p);
        this.progress.remove(uuid);

        Player player = this.plugin.getServer().getPlayer(uuid);
        BPlayer bPlayer = this.plugin.getGame().getPlayer(uuid);
        if (player == null || bPlayer == null)
            return ;
        player.getInventory().remove(Material.WOOL);
        Ball ball = new Ball(((CraftWorld)player.getWorld()).getHandle(), uuid, player.getLocation(), player.getLocation().getDirection().multiply(power * 0.8F), bPlayer.getBallDescription());
        ((CraftWorld)player.getWorld()).getHandle().addEntity(ball);
        player.setExp(0F);
    }

    private static float progressToPercent(float progress)
    {
        float result = progress * 10;
        if (result > 140)
            result = 140;
        if (result > 100)
            result = 200 - result;
        return result / 100;
    }
}
