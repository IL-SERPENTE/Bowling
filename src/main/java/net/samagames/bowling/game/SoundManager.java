package net.samagames.bowling.game;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongEndEvent;
import net.samagames.bowling.Bowling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Rigner for project Bowling.
 */
public class SoundManager implements Listener
{
    private Bowling plugin;
    private RadioSongPlayer songPlayer;

    public SoundManager(Bowling plugin)
    {
        try
        {
            this.plugin = plugin;
            Song song = NBSDecoder.parse(new File("background.nbs"));
            if (song == null)
                throw new FileNotFoundException();
            this.songPlayer = new RadioSongPlayer(song);
            this.songPlayer.setAutoDestroy(true);
            this.songPlayer.setPlaying(true);
            this.songPlayer.setVolume((byte)50);
            this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        }
        catch (Exception ignored)
        {
            this.songPlayer = null;
            this.plugin.getLogger().warning("Could not read music file, this feature is now disabled.");
        }
    }

    public void addPlayer(Player player)
    {
        if (this.songPlayer != null)
            this.songPlayer.addPlayer(player);
    }

    public void removePlayer(Player player)
    {
        if (this.songPlayer != null)
            this.songPlayer.removePlayer(player);
    }

    @EventHandler
    public void onSoundEnd(SongEndEvent event)
    {
        if (this.songPlayer != event.getSongPlayer())
            return ;
        this.songPlayer = new RadioSongPlayer(NBSDecoder.parse(new File("background.nbs")));
        this.plugin.getServer().getOnlinePlayers().forEach(this.songPlayer::addPlayer);
    }
}
