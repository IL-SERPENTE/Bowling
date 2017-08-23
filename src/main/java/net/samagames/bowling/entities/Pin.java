package net.samagames.bowling.entities;

import net.minecraft.server.v1_10_R1.EntityArmorStand;
import net.minecraft.server.v1_10_R1.World;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
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
public class Pin extends EntityArmorStand
{
    public static final double PRECISION = 60D;
    public static final List<Pin> PINS = new LinkedList<>();
    public static final List<Pair<Pin, Pin>> COLLISIONS = new LinkedList<>();

    protected static Field lockSlotsField;
    protected Vector vector;

    Pin(World world)
    {
        super(world);
        this.die();
    }

    public Pin(World world, Location location)
    {
        this(world, location, new Vector().zero());
    }

    Pin(World world, Location location, Vector vector)
    {
        super(world);
        this.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw() + 90F, location.getPitch());

        ArmorStand bukkitArmorStand = (ArmorStand)this.getBukkitEntity();
        bukkitArmorStand.setVisible(false);
        bukkitArmorStand.setGravity(false);
        bukkitArmorStand.setInvulnerable(true);
        bukkitArmorStand.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
        bukkitArmorStand.setAI(false);
        this.vector = vector;

        try
        {
            if (Pin.lockSlotsField != null)
                Pin.lockSlotsField.set(this, Integer.MAX_VALUE);
        }
        catch (ReflectiveOperationException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Reflection error", ex);
        }

        Pin.PINS.add(this);
    }

    @Override
    public void die()
    {
        Pin.PINS.remove(this);
        super.die();
    }

    @Override
    public void n()
    {
    }

    public double getWeight()
    {
        return 1.5D;
    }

    public double getRadius()
    {
        return 0.20D;
    }

    public double getFrictionRatio()
    {
        return 0.6D;
    }

    public void movePin()
    {
        if (this.dead || this.vector.lengthSquared() < 0.01)
            return ;

        Location location = this.getBukkitEntity().getLocation().add(0D, 1D, 0D);
        boolean ok[] = new boolean[3];
        if (ok[0] = location.clone().add(this.vector.getX() / Pin.PRECISION, 0D, 0D).getBlock().getType() == Material.AIR)
            this.locX = this.locX + this.vector.getX() / Pin.PRECISION;
        if (ok[1] = location.clone().add(0D, this.vector.getY() / Pin.PRECISION, 0D).getBlock().getType() == Material.AIR)
            this.locY = this.locY + this.vector.getY() / Pin.PRECISION;
        if (ok[2] = location.clone().add(0D, 0D, this.vector.getZ() / Pin.PRECISION).getBlock().getType() == Material.AIR)
            this.locZ = this.locZ + this.vector.getZ() / Pin.PRECISION;
        if (ok[0] || ok[1] || ok[2])
            this.positionChanged = true;
    }

    public void onPreMove()
    {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.vector.multiply(this.getFrictionRatio());
        this.vector.setY(this instanceof StandBall ? -2 : 0);
    }

    public void onPostMove()
    {
    }

    public void checkCollide()
    {
        if (this.dead)
            return ;
        Pin.PINS.stream().filter(entity ->
        {
            Location loc1 = this.getBukkitEntity().getLocation();
            Location loc2 = entity.getBukkitEntity().getLocation();
            loc1.setY(0D);
            loc2.setY(0D);
            return !entity.dead && entity != this && loc1.distanceSquared(loc2) <= Math.pow(this.getRadius() + entity.getRadius(), 2);
        }).forEach(this::collide);
    }

    private void collide(Pin pin)
    {
        for (Pair<Pin, Pin> pair : Pin.COLLISIONS)
            if ((pair.getLeft().getId() == this.getId() && pair.getRight().getId() == pin.getId()) || (pair.getLeft().getId() == pin.getId() && pair.getRight().getId() == this.getId()))
                return ;
        Pin.COLLISIONS.add(Pair.of(pin, this));

        //Algorithm from http://www.gamasutra.com/view/feature/131424/pool_hall_lessons_fast_accurate_.php?page=3

        Vector n = new Vector(this.locX - pin.locX, this.locY - pin.locY, this.locZ - pin.locZ).normalize();

        double a1 = this.vector.dot(n);
        double a2 = pin.vector.dot(n);

        double optimizedP = (2.0 * (a1 - a2)) / (this.getWeight() + pin.getWeight());

        Vector v1 = this.vector.subtract(n.clone().multiply(optimizedP * pin.getWeight()));
        Vector v2 = pin.vector.subtract(n.clone().multiply(optimizedP * this.getWeight()));

        this.vector = v1;
        pin.vector = v2;
    }

    public void setLocation(Location location)
    {
        this.lastX = this.locX = location.getX();
        this.lastY = this.locY = location.getY();
        this.lastZ = this.locZ = location.getZ();
        this.positionChanged = true;
    }

    static
    {
        try
        {
            Pin.lockSlotsField = EntityArmorStand.class.getDeclaredField("bA");
            Pin.lockSlotsField.setAccessible(true);
        }
        catch (ReflectiveOperationException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Reflection error", ex);
            Pin.lockSlotsField = null;
        }
    }
}
