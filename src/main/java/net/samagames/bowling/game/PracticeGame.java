package net.samagames.bowling.game;

import net.samagames.bowling.Bowling;
import net.samagames.bowling.util.PracticeWinTemplate;
import org.bukkit.ChatColor;

/*
 * This file is part of Bowling.
 *
 * Bowling is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bowling is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bowling.  If not, see <http://www.gnu.org/licenses/>.
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
