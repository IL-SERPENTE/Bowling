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

    void addPlayer(Player player)
    {
        if (this.songPlayer != null)
            this.songPlayer.addPlayer(player);
    }

    void removePlayer(Player player)
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
