package net.samagames.bowling.image;

import net.samagames.bowling.Bowling;

import java.util.logging.Level;

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
public class ImageManager
{
    private Bowling plugin;
    private ImageConfiguration imageConfiguration;

    public ImageManager(Bowling plugin)
    {
        this.plugin = plugin;
        try
        {
            this.imageConfiguration = new ImageConfiguration();
        }
        catch (Exception ignored)
        {
            this.imageConfiguration = null;
            this.plugin.getLogger().warning("Could not read image configuration, this feature is now disabled.");
        }
    }

    public String createImage(String player, int[] scores)
    {
        try
        {
            BImage bImage = new BImage(this.imageConfiguration);
            //bImage.draw(player, scores);
            String url = bImage.send();
            this.plugin.getLogger().info("Created score image for player " + player + " -> " + url);
            return url;
        }
        catch (Exception ex)
        {
            this.plugin.getLogger().log(Level.SEVERE, "Can't upload image", ex);
            return null;
        }
    }

    public boolean isEnabled()
    {
        return this.imageConfiguration != null;
    }
}
