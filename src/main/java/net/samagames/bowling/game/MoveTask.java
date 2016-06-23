package net.samagames.bowling.game;

import net.samagames.bowling.entities.Pin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project Bowling.
 */
public class MoveTask extends BukkitRunnable
{
    @Override
    public void run()
    {
        List<Pin> pinList = new ArrayList<>(Pin.PINS);
        pinList.forEach(Pin::onPreMove);
        for (int i = 0; i < Pin.PRECISION; i++)
        {
            Pin.COLLISIONS.clear();
            pinList.forEach(Pin::checkCollide);
            Pin.COLLISIONS.clear();
            pinList.forEach(Pin::movePin);
        }
    }
}
