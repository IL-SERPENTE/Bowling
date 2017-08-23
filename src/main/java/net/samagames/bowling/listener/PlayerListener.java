package net.samagames.bowling.listener;

import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Ball;
import net.samagames.bowling.entities.StandBall;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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
public class PlayerListener implements Listener
{
    private Bowling plugin;

    public PlayerListener(Bowling plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event)
    {
        this.onInteract(event.getPlayer(), event.getRightClicked());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        event.setCancelled(true);
        if (event.getDamager() instanceof Player)
            this.onInteract((Player)event.getDamager(), event.getEntity());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    private void onInteract(Player player, Entity entity)
    {
        if (entity != null && ((CraftEntity)entity).getHandle() instanceof StandBall && !(((CraftEntity)entity).getHandle() instanceof Ball))
        {
            StandBall standBall = (StandBall)((CraftEntity)entity).getHandle();
            if (standBall.getOwner().equals(player.getUniqueId()))
            {
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), ((ArmorStand)standBall.getBukkitEntity()).getHelmet());
                standBall.die();
                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> Titles.sendTitle(player, 10, 200, 10, "", ChatColor.GOLD + "Tirez en restant appuyé !"), 20L);
            }
        }
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event)
    {
        event.setCancelled(true);
    }
}
