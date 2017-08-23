package net.samagames.bowling.game;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.shops.IPlayerShop;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Ball;
import net.samagames.bowling.entities.StandBall;
import net.samagames.bowling.guis.CosmeticsGui;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
public class BPlayer extends GamePlayer
{
    private BowlingTrack bowlingTrack;
    private Ball.BallDescription ballDescription;
    private int[] score;
    private int currentShoot;
    private ObjectiveSign objectiveSign;

    public BPlayer(Player player)
    {
        super(player);
        this.bowlingTrack = null;
        this.ballDescription = new Ball.BallDescription(Ball.BallWeight.MEDIUM, (short)0, null);
        this.score = new int[21];
        this.objectiveSign = null;
        this.resetScore(player);
    }

    void loadFromDB()
    {
        try
        {
            IPlayerShop iPlayerShop = SamaGamesAPI.get().getShopsManager().getPlayer(this.uuid);

            for (int i = 0; i < 16; i++)
                if (iPlayerShop.getTransactionsByID(i + 147) != null && iPlayerShop.isSelectedItem(i + 147))
                    this.ballDescription.setBallColor((short)i);

            for (StandBall.BallWeight ballWeight : StandBall.BallWeight.values())
                if (iPlayerShop.getTransactionsByID(ballWeight.getItemId()) != null && iPlayerShop.isSelectedItem(ballWeight.getItemId()))
                    this.ballDescription.setBallWeight(ballWeight);

            for (CosmeticsGui.ParticleWrapper triple : CosmeticsGui.PARTICLES)
                if (iPlayerShop.getTransactionsByID(triple.getItemId()) != null && iPlayerShop.isSelectedItem(triple.getItemId()))
                    this.ballDescription.setParticle(triple.getParticle());

        } catch (Exception ignored) {}
    }

    public BowlingTrack getBowlingTrack()
    {
        return this.bowlingTrack;
    }

    void setBowlingTrack(BowlingTrack bowlingTrack)
    {
        this.bowlingTrack = bowlingTrack;
    }

    public Ball.BallDescription getBallDescription()
    {
        return ballDescription;
    }

    void resetScore(Player player)
    {
        if (this.objectiveSign != null)
            this.objectiveSign.removeReceiver(player);

        this.objectiveSign = new ObjectiveSign("bowling", ChatColor.GOLD + "Bowling");
        this.objectiveSign.addReceiver(player);
        Arrays.fill(this.score, -1);
        this.currentShoot = 0;
    }

    void setScore(int where, int score)
    {
        if (where >= 0 && where < this.score.length)
            this.score[where] = score;
    }

    int getScore(int where)
    {
        return this.score[where];
    }

    int getCurrentShoot()
    {
        return this.currentShoot;
    }

    void setCurrentShoot(int currentShoot)
    {
        this.currentShoot = currentShoot;
    }

    public int getTotalScore()
    {
        int result = 0;

        for (int i = 0; i < this.score.length; i += 2)
        {
            if (this.score[i] == 10) //STRIKE
            {
                int n = 0;
                for (int j = i + 2; j < this.score.length && n < 2; j++)
                    if (this.score[j] != -1)
                    {
                        result += this.score[j];
                        n++;
                    }
                result += 10;
            }
            else if (i != 20 && this.score[i] + this.score[i + 1] == 10) //SPARE
            {
                if (this.score[i + 2] != -1)
                    result += this.score[i + 2];
                result += 10;
            }
            else if (this.score[i] != -1) //OTHER
            {
                result += this.score[i];
                if (i != 20 && this.score[i + 1] != -1)
                    result += this.score[i + 1];
            }
        }

        return result;
    }

    void updateScoreboard(Bowling bowling)
    {
        this.objectiveSign.setLine(0, " ");
        this.objectiveSign.setLine(1, ChatColor.GRAY + "Joueurs : " + ChatColor.WHITE + bowling.getGame().getInGamePlayers().size());
        this.objectiveSign.setLine(2, "  ");
        if (this.bowlingTrack != null)
        {
            this.objectiveSign.setLine(3, ChatColor.GRAY + "Score : " + ChatColor.WHITE + this.getTotalScore());
            int launch = this.getCurrentShoot() / 2 + 1;
            this.objectiveSign.setLine(4, ChatColor.GRAY + "Lancer : " + ChatColor.WHITE + (launch == 11 ? this.getCurrentShoot() == 22 ? "Fini" : "X" : String.valueOf(launch)));
            this.objectiveSign.setLine(5, "   ");
        }
        this.objectiveSign.updateLines();
    }

    @Override
    public void setSpectator()
    {
        this.spectator = true;
    }

    int[] getScores()
    {
        return this.score;
    }
}
