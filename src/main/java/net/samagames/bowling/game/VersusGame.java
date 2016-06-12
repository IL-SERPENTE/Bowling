package net.samagames.bowling.game;

import net.samagames.api.games.Status;
import net.samagames.bowling.Bowling;
import org.bukkit.entity.Player;

/**
 * Created by Rigner for project Bowling.
 */
public class VersusGame extends AbstractGame
{
    public VersusGame(Bowling plugin)
    {
        super(plugin, "Versus");
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
            this.coherenceMachine.getMessageManager().writePlayerQuited(player);
        super.handleLogout(player);
    }

    @Override
    public void onPlayerEnd(BPlayer bPlayer)
    {
        //TODO
    }

    @Override
    public void startGame()
    {
        super.startGame();
        //TODO
    }
}
