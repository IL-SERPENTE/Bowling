package net.samagames.bowling.game;

import net.samagames.bowling.entities.Pin;
import org.bukkit.scheduler.BukkitRunnable;

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
        pinList.forEach(Pin::onPostMove);
    }
}
