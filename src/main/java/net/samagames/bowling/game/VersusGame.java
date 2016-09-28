package net.samagames.bowling.game;

import net.samagames.api.games.Status;
import net.samagames.bowling.Bowling;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project Bowling.
 */
public class VersusGame extends AbstractGame
{
    private boolean end;

    public VersusGame(Bowling plugin)
    {
        super(plugin, "Versus");
        this.end = false;
    }

    @Override
    public void handleLogin(Player player)
    {
        super.handleLogin(player);
        this.coherenceMachine.getMessageManager().writePlayerJoinToAll(player);
    }

    @Override
    public void handleLogout(Player player)
    {
        BPlayer bPlayer;
        if (this.status != Status.FINISHED && (bPlayer = this.getPlayer(player.getUniqueId())) != null && !bPlayer.isSpectator())
        {
            this.coherenceMachine.getMessageManager().writePlayerQuited(player);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::checkStump, 40L);
        }
        super.handleLogout(player);
    }

    @Override
    public void startGame()
    {
        super.startGame();
        this.getInGamePlayers().forEach((uuid, bPlayer) ->
        {
            Player player = bPlayer.getPlayerIfOnline();
            if (player != null)
                this.playPlayer(player);
        });
    }

    @Override
    public void stumpPlayer(Player player)
    {
        BPlayer bPlayer;
        if (!this.isGameStarted() || (bPlayer = this.getPlayer(player.getUniqueId())) == null || bPlayer.isSpectator() || bPlayer.isModerator())
            return ;
        bPlayer.setSpectator();
        this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + player.getDisplayName() + " a fini de jouer. Score : " + ChatColor.WHITE + ChatColor.BOLD + bPlayer.getTotalScore(), true);
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::checkStump, 40L);
    }

    private void checkStump()
    {
        if (this.end)
            return ;
        List<BPlayer> players = new ArrayList<>(this.getRegisteredGamePlayers().values());
        for (BPlayer bPlayer : players)
            if (!bPlayer.isSpectator() || bPlayer.getCurrentShoot() != 22)
                return ;
        this.end = true;
        players.sort((first, second) -> first.getTotalScore() - second.getTotalScore());
        if (players.size() >= 2)
            this.coherenceMachine.getTemplateManager().getPlayerLeaderboardWinTemplate().execute(
                    players.get(0).getPlayerIfOnline(), players.get(1).getPlayerIfOnline(), players.size() > 2 ? players.get(2).getPlayerIfOnline() : null,
                    players.get(0).getTotalScore(), players.get(1).getTotalScore(), players.size() > 2 ? players.get(2).getTotalScore() : 0);
        else if (players.size() == 1)
            this.coherenceMachine.getTemplateManager().getPlayerWinTemplate().execute(players.get(0).getPlayerIfOnline(), players.get(0).getTotalScore());
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::handleGameEnd, 70L);
    }
}
