package net.samagames.bowling.game;

import net.samagames.bowling.Bowling;
import net.samagames.bowling.util.PracticeWinTemplate;
import org.bukkit.ChatColor;

/**
 * Created by Rigner for project Bowling.
 */
public class PracticeGame extends AbstractGame
{
    public PracticeGame(Bowling plugin)
    {
        super(plugin, "Practice");
    }

    @Override
    public void handlePostRegistration()
    {
        super.handlePostRegistration();
        this.beginTimer.cancel();
    }

    @Override
    public void startGame()
    {
        /* No start */
    }

    @Override
    public void onPlayerEnd(BPlayer bPlayer)
    {
        super.onPlayerEnd(bPlayer);
        new PracticeWinTemplate().execute(bPlayer);
        bPlayer.getPlayerIfOnline().sendMessage(this.coherenceMachine.getGameTag() + ChatColor.YELLOW + " Merci d'avoir joué, revenez quand vous voulez !");
    }
}
