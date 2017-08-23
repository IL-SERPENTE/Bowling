package net.samagames.bowling.util;

import net.samagames.api.games.themachine.messages.templates.BasicMessageTemplate;
import net.samagames.bowling.game.BPlayer;
import net.samagames.tools.chat.ChatUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

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
public class PracticeWinTemplate
{
    private List<String> prepare(BPlayer bPlayer)
    {
        List<String> lines = new ArrayList<>();

        lines.add(ChatUtils.getCenteredText(ChatColor.WHITE + "•" + ChatColor.BOLD + " Score " + ChatColor.WHITE + "•"));
        int score = bPlayer.getTotalScore();
        lines.add((score < 100 ? " " : "") + ChatUtils.getCenteredText(ChatColor.GRAY.toString() + score));
        lines.add("");

        lines.add(ChatUtils.getCenteredText(ChatColor.WHITE + "★" + ChatColor.BOLD + " Record personnel " + ChatColor.WHITE + "★"));
        lines.add(" " + ChatUtils.getCenteredText(ChatColor.GRAY + "Inconnu"));
        lines.add("");

        return lines;
    }

    public void execute(BPlayer bPlayer)
    {
        new BasicMessageTemplate().execute(this.prepare(bPlayer));
    }
}
