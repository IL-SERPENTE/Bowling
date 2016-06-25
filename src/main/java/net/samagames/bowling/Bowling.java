package net.samagames.bowling;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.bowling.game.*;
import net.samagames.bowling.image.ImageManager;
import net.samagames.bowling.listener.PlayerListener;
import net.samagames.bowling.listener.ShootListener;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Rigner for project Bowling.
 */
public class Bowling extends JavaPlugin
{
    private SamaGamesAPI samaGamesAPI;
    private AbstractGame game;
    private boolean practice;
    private ImageManager imageManager;
    private SoundManager soundManager;

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable()
    {
        this.samaGamesAPI = SamaGamesAPI.get();
        this.imageManager = new ImageManager(this);
        this.soundManager = new SoundManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ShootListener(this), this);
        this.getServer().getScheduler().runTaskTimer(this, new MoveTask(), 1L, 1L);

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
