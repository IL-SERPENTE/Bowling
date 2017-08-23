package net.samagames.bowling.entities;

import net.minecraft.server.v1_10_R1.World;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.game.BPlayer;
import net.samagames.bowling.game.BowlingTrack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;

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
public class Ball extends StandBall
{
    private Bowling bowling;
    private BowlingTrack track;
    private Vector initialVector;

    public Ball(World world)
    {
        super(world);
    }

    public Ball(World world, UUID owner, Location location, Vector vector, BallDescription ballDescription)
    {
        super(world, owner, location, vector, ballDescription);
        this.initialVector = vector.clone();
        this.vector.setY(-2);
        this.bowling = (Bowling)Bukkit.getPluginManager().getPlugin("Bowling");
        BPlayer bPlayer = this.bowling.getGame().getPlayer(owner);
        this.track = bPlayer != null ? bPlayer.getBowlingTrack() : null;
    }

    @Override
    public void movePin()
    {
        if (!this.dead && this.track != null && (this.track.isInEndZone(this.getBukkitEntity().getLocation()) || Math.abs(this.initialVector.angle(this.vector)) > Math.PI / 2 || this.vector.lengthSquared() < 0.01))
        {
            this.die();
            this.bowling.getServer().getScheduler().runTaskLater(this.bowling, () -> this.track.onShootEnd(), 40L);
            return ;
        }
        super.movePin();
    }

    @Override
    public void onPostMove()
    {
        ArmorStand bukkitArmorStand = (ArmorStand) this.getBukkitEntity();
        EulerAngle angle = bukkitArmorStand.getHeadPose();
        angle.setZ(angle.getZ() + Math.sqrt(Math.pow(this.locX - this.lastX, 2) + Math.pow(this.locZ - this.lastZ, 2)) / 2D);
        bukkitArmorStand.setHeadPose(angle);
    }
}
