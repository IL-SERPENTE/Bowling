package net.samagames.bowling.entities;

import net.minecraft.server.v1_9_R2.World;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.game.BPlayer;
import net.samagames.bowling.game.BowlingTrack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Created by Rigner for project Bowling.
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
}
