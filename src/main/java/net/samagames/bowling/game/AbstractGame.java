package net.samagames.bowling.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import net.samagames.api.games.Game;
import net.samagames.api.games.Status;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Pin;
import net.samagames.bowling.entities.StandBall;
import net.samagames.bowling.guis.ManagerGui;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.RulesBook;
import net.samagames.tools.Titles;
import net.samagames.tools.npc.nms.CustomNPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Rigner for project Bowling.
 */
public abstract class AbstractGame extends Game<BPlayer>
{
    protected Bowling plugin;
    protected List<BowlingTrack> tracks;
    protected Location spawn;
    protected ItemStack rulesBook;

    public AbstractGame(Bowling plugin, String description)
    {
        super("bowling", "Bowling", description, BPlayer.class);
        this.plugin = plugin;
        this.tracks = new ArrayList<>();

        this.gameManager.getGameProperties().getConfig("tracks", new JsonArray()).getAsJsonArray().forEach(json ->
        {
            BowlingTrack bowlingTrack = BowlingTrack.fromJson(this.plugin, json);
            if (bowlingTrack != null)
                this.tracks.add(bowlingTrack);
        });
        this.tracks.forEach(track -> track.respawn(true));
        this.tracks.forEach(track -> track.setScore(0));

        CustomNPC customNPC = this.plugin.getSamaGamesAPI().getNPCManager().createNPC(LocationUtils.str2loc(this.gameManager.getGameProperties().getConfig("npc", new JsonPrimitive("world, 0, 4, 0")).getAsString()), UUID.fromString("6715ffeb-966f-45a1-a19f-dc9c07e039b3"), "Samaritan");
        customNPC.setCallback((right, player) ->
        {
            BPlayer bPlayer = this.getPlayer(player.getUniqueId());
            if (bPlayer != null && !bPlayer.isSpectator())
                this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, new ManagerGui(this.plugin, bPlayer.getBowlingTrack() != null));
        });

        this.spawn = LocationUtils.str2loc(this.plugin.getSamaGamesAPI().getGameManager().getGameProperties().getConfig("spawn", new JsonPrimitive("world, 0, 4, 0")).getAsString());

        this.rulesBook = new RulesBook("Bowling").addOwner("Rigner").addPage("Practice", "").addPage("Tournoi", "").toItemStack();

        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> this.getInGamePlayers().forEach((uuid, bPlayer) -> bPlayer.updateScoreboard(this.plugin)), 20L, 20L);
    }

    @Override
    public void handleLogin(Player player)
    {
        try
        {
            BPlayer bPlayer = this.gamePlayerClass.getConstructor(Player.class).newInstance(player);
            bPlayer.handleLogin(false);
            this.gamePlayers.put(player.getUniqueId(), bPlayer);
            bPlayer.updateScoreboard(this.plugin);
            Titles.sendTitle(player, 20, 60, 20, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + this.gameName, ChatColor.AQUA + this.gameDescription);
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, bPlayer::loadFromDB);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException ex)
        {
            this.plugin.getSamaGamesAPI().getPlugin().getLogger().log(Level.SEVERE, "Can't instantiate GamePlayer object", ex);
        }

        player.teleport(this.spawn);
        player.getInventory().clear();
        player.getInventory().setItem(8, this.rulesBook);
        player.setExp(0F);
        player.setLevel(0);
    }

    @Override
    public void handleLogout(Player player)
    {
        this.stumpPlayer(player);

        if (this.status != Status.FINISHED)
        {
            if (this.gamePlayers.containsKey(player.getUniqueId()))
            {
                this.gamePlayers.get(player.getUniqueId()).handleLogout();
                if (this.status != Status.IN_GAME)
                    this.gamePlayers.remove(player.getUniqueId());

                this.gameManager.refreshArena();
            }
        }
    }

    public void playPlayer(Player player)
    {
        BPlayer bPlayer = this.getPlayer(player.getUniqueId());
        if (bPlayer == null || bPlayer.isSpectator() || bPlayer.isModerator() || bPlayer.getBowlingTrack() != null)
            return ;

        final BowlingTrack track = this.tracks.stream().filter(t -> t.getPlayer() == null).findAny().orElse(null);

        if (track == null)
        {
            player.sendMessage(ChatColor.RED + "Aucune piste de libre.");
            return ;
        }

        player.closeInventory();
        track.setPlayer(bPlayer);
        bPlayer.setBowlingTrack(track);
        player.teleport(track.getSpawn());
        track.respawn(true);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, track::spawnBall, 50L);
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> Titles.sendTitle(player, 10, 200, 10, "", ChatColor.GOLD + "Prenez votre boule"), 35L);
    }

    public void stumpPlayer(Player player)
    {
        if (player == null)
            return ;
        BPlayer bPlayer = this.getPlayer(player.getUniqueId());
        if (bPlayer == null || bPlayer.isSpectator() || bPlayer.isModerator() || bPlayer.getBowlingTrack() == null || status == Status.FINISHED)
            return ;

        List<Pin> pins = new LinkedList<>(Pin.PINS);
        pins.stream().filter(pin -> pin instanceof StandBall && ((StandBall)pin).getOwner().equals(player.getUniqueId())).forEach(Pin::die);

        bPlayer.resetScore(player);
        bPlayer.getBowlingTrack().respawn(true);
        bPlayer.getBowlingTrack().setScore(0);
        bPlayer.getBowlingTrack().setPlayer(null);
        bPlayer.setBowlingTrack(null);
        player.teleport(this.spawn);
        player.getInventory().clear();
        player.getInventory().setItem(8, this.rulesBook);
    }

    public void onPlayerEnd(BPlayer bPlayer)
    {
        Player player = bPlayer.getPlayerIfOnline();
        if (player == null)
            return ;
        this.stumpPlayer(player);
        if (this.plugin.getImageManager().isEnabled())
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () ->
            {
                String url = this.plugin.getImageManager().createImage(player.getName(), bPlayer.getScores());
                player.sendMessage(this.coherenceMachine.getGameTag() + " " + (url == null ? ChatColor.RED + "Erreur lors de la création de votre résumé de partie, contactez un administrateur." : ChatColor.YELLOW + "Retrouvez votre score ici : " + ChatColor.AQUA + ChatColor.BOLD + url));
            });
    }
}
