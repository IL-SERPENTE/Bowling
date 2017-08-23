package net.samagames.bowling;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.bowling.game.*;
import net.samagames.bowling.image.ImageManager;
import net.samagames.bowling.listener.PlayerListener;
import net.samagames.bowling.listener.ShootListener;
import org.bukkit.plugin.java.JavaPlugin;

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
public class Bowling extends JavaPlugin
{
    private SamaGamesAPI samaGamesAPI;
    private AbstractGame game;
    private boolean practice;
    private ImageManager imageManager;
    private SoundManager soundManager;

    @Override
    public void onEnable()
    {
        this.samaGamesAPI = SamaGamesAPI.get();
        this.imageManager = new ImageManager(this);
        this.soundManager = new SoundManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ShootListener(this), this);
        new MoveTask().runTaskTimer(this, 1L, 1L);

        this.practice = this.samaGamesAPI.getGameManager().getGameProperties().getConfig("practice", new JsonPrimitive(true)).getAsBoolean();
        this.game = this.practice ? new PracticeGame(this) : new VersusGame(this);
        this.samaGamesAPI.getGameManager().registerGame(this.game);
        this.samaGamesAPI.getGameManager().setLegacyPvP(true);
    }

    public SamaGamesAPI getSamaGamesAPI()
    {
        return this.samaGamesAPI;
    }

    public AbstractGame getGame()
    {
        return this.game;
    }

    public boolean isPractice()
    {
        return this.practice;
    }

    public ImageManager getImageManager()
    {
        return this.imageManager;
    }

    public SoundManager getSoundManager()
    {
        return this.soundManager;
    }
}
