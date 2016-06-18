package net.samagames.bowling.image;

import net.samagames.bowling.Bowling;

import java.util.logging.Level;

/**
 * Created by Rigner for project Bowling.
 */
public class ImageManager
{
    private Bowling plugin;
    private ImageConfiguration imageConfiguration;

    public ImageManager(Bowling plugin)
    {
        this.plugin = plugin;
        this.imageConfiguration = new ImageConfiguration();
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
}
