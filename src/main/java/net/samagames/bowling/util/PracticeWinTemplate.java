package net.samagames.bowling.util;

import net.samagames.api.games.themachine.messages.templates.BasicMessageTemplate;
import net.samagames.bowling.game.BPlayer;
import net.samagames.tools.chat.ChatUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project Bowling.
 */
public class PracticeWinTemplate
{
    public List<String> prepare(BPlayer bPlayer)
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
