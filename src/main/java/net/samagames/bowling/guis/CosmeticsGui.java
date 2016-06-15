package net.samagames.bowling.guis;

import net.samagames.api.gui.AbstractGui;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Ball;
import net.samagames.bowling.entities.StandBall;
import net.samagames.bowling.game.BPlayer;
import net.samagames.tools.GlowEffect;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project Bowling.
 */
public class CosmeticsGui extends AbstractGui
{
    public static final List<Pair<ItemStack, Particle>> PARTICLES = new ArrayList<>();

    private Bowling plugin;

    public CosmeticsGui(Bowling plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.plugin.getServer().createInventory(null, 45, "Cosmétiques");
        this.update(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        this.setSlotData(ChatColor.DARK_AQUA + "Masse", new ItemStack(Material.ANVIL), 11, null, "mass");
        this.setSlotData(ChatColor.GREEN + "Couleur", new ItemStack(Material.WOOL), 13, null, "color");
        this.setSlotData(ChatColor.DARK_PURPLE + "Effets", new ItemStack(Material.BLAZE_POWDER), 15, null, "particle");
        this.setSlotData(ChatColor.GREEN + "Retour", new ItemStack(Material.EMERALD), 31, null, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        AbstractGui gui = null;
        switch (action)
        {
            case "mass":
                gui = new MassSelectorGui(this.plugin);
                break ;
            case "color":
                gui = new ColorSelectorGui(this.plugin);
                break ;
            case "particle":
                gui = new ParticleSelectorGui(this.plugin);
                break ;
            case "back":
                gui = new ManagerGui(this.plugin);
            default:
                break ;
        }
        if (gui != null)
            this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, gui);
    }

    static class MassSelectorGui extends AbstractGui
    {
        private Bowling plugin;

        MassSelectorGui(Bowling plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public void display(Player player)
        {
            this.inventory = player.getServer().createInventory(null, 45, "Selecteur - Masse");
            this.update(player);
            player.openInventory(this.inventory);
        }

        @Override
        public void update(Player player)
        {
            BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
            if (bPlayer == null)
                return ;
            this.setSlotData(ChatColor.GREEN + "Léger (3 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.LIGHT ? GlowEffect.addGlow(new ItemStack(Material.SLIME_BALL)) : new ItemStack(Material.SLIME_BALL), 11, null, "light");
            this.setSlotData(ChatColor.DARK_GREEN + "Moyen (5 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.MEDIUM ? GlowEffect.addGlow(new ItemStack(Material.SNOW_BALL)) : new ItemStack(Material.SNOW_BALL), 13, null, "medium");
            this.setSlotData(ChatColor.DARK_GRAY + "Lourd (7 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.HEAVY ? GlowEffect.addGlow(new ItemStack(Material.FIREBALL)) : new ItemStack(Material.FIREBALL), 15, null, "heavy");

            this.setSlotData(ChatColor.GREEN + "Retour", new ItemStack(Material.EMERALD), 31, null, "back");
        }

        @Override
        public void onClick(Player player, ItemStack stack, String action)
        {
            Ball.BallWeight ballWeight = null;
            switch (action)
            {
                case "light":
                    ballWeight = Ball.BallWeight.LIGHT;
                    break ;
                case "medium":
                    ballWeight = Ball.BallWeight.MEDIUM;
                    break ;
                case "heavy":
                    ballWeight = Ball.BallWeight.HEAVY;
                    break ;
                case "back":
                    this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, new CosmeticsGui(this.plugin));
                default:
                    break ;
            }
            BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
            if (bPlayer != null)
            {
                bPlayer.getBallDescription().setBallWeight(ballWeight);
                player.sendMessage(ChatColor.YELLOW + "La masse de votre boule a bien été changée.");
            }
        }
    }

    class ColorSelectorGui extends AbstractGui
    {
        private Bowling plugin;

        public ColorSelectorGui(Bowling plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public void display(Player player)
        {
            this.inventory = this.plugin.getServer().createInventory(null, 54, "Selecteur - Couleur");
            this.update(player);
            player.openInventory(this.inventory);
        }

        @Override
        public void update(Player player)
        {
            BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
            if (bPlayer == null)
                return ;
            for (short i = 0; i < 16; i++)
                this.setSlotData(StandBall.WOOL_NAMES[i], bPlayer.getBallDescription().getBallColor() == i ? GlowEffect.addGlow(new ItemStack(Material.WOOL, 1, i)) : new ItemStack(Material.WOOL, 1, i), i + (i > 13 ? 14 : i > 6 ? 12 : 10), null, "color");
            this.setSlotData(ChatColor.GREEN + "Retour", new ItemStack(Material.EMERALD), 40, null, "back");
        }

        @Override
        public void onClick(Player player, ItemStack stack, String action)
        {
            switch (action)
            {
                case "color":
                    BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
                    if (bPlayer != null)
                    {
                        bPlayer.getBallDescription().setBallColor(stack.getDurability());
                        player.sendMessage(ChatColor.YELLOW + "La couleur de votre boule a bien été changée.");
                    }
                    break ;
                case "back":
                    this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, new CosmeticsGui(this.plugin));
                default:
                    break ;
            }
        }
    }

    class ParticleSelectorGui extends AbstractGui
    {
        private Bowling plugin;

        public ParticleSelectorGui(Bowling plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public void display(Player player)
        {
            this.inventory = this.plugin.getServer().createInventory(null, 54, "Selecteur - Particules");
            this.update(player);
            player.openInventory(this.inventory);
        }

        @Override
        public void update(Player player)
        {
            BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
            if (bPlayer == null)
                return ;
            int i = 0;
            for (Pair<ItemStack, Particle> particlePair : CosmeticsGui.PARTICLES)
            {
                this.setSlotData(particlePair.getValue() == bPlayer.getBallDescription().getParticle() ? GlowEffect.addGlow(particlePair.getKey()) : particlePair.getKey(), i + (i > 13 ? 14 : i > 6 ? 12 : 10), "particle;" + particlePair.getValue());
                i++;
            }
            this.setSlotData(ChatColor.GREEN + "Retour", new ItemStack(Material.EMERALD), 40, null, "back");
        }

        @Override
        public void onClick(Player player, ItemStack stack, String action)
        {
            if (action == null)
                return ;
            String[] split = action.split(";");
            switch (split[0])
            {
                case "particle":
                    Particle particle = Particle.valueOf(split[1]);
                    BPlayer bPlayer = this.plugin.getGame().getPlayer(player.getUniqueId());
                    if (bPlayer != null)
                    {
                        bPlayer.getBallDescription().setParticle(particle);
                        player.sendMessage(ChatColor.YELLOW + "Les particules de votre boule ont bien été changées.");
                    }
                    break ;
                case "back":
                    this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, new CosmeticsGui(this.plugin));
                default:
                    break ;
            }
        }
    }

    private static void registerParticle(ItemStack itemStack, String name, Particle particle)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        CosmeticsGui.PARTICLES.add(Pair.of(itemStack, particle));
    }

    static
    {
        CosmeticsGui.registerParticle(new ItemStack(Material.RED_ROSE), ChatColor.RED + "Coeur", Particle.HEART);
        CosmeticsGui.registerParticle(new ItemStack(Material.OBSIDIAN), ChatColor.DARK_PURPLE + "Portail", Particle.PORTAL);
        CosmeticsGui.registerParticle(new ItemStack(Material.EMERALD), ChatColor.GREEN + "Emeraude", Particle.VILLAGER_HAPPY);
        CosmeticsGui.registerParticle(new ItemStack(Material.END_ROD), ChatColor.WHITE + "End", Particle.END_ROD);
        CosmeticsGui.registerParticle(new ItemStack(Material.FLINT_AND_STEEL), ChatColor.DARK_RED + "Flame", Particle.FLAME);
        CosmeticsGui.registerParticle(new ItemStack(Material.LAVA_BUCKET), ChatColor.DARK_RED + "Lave", Particle.LAVA);
        CosmeticsGui.registerParticle(new ItemStack(Material.JUKEBOX), ChatColor.DARK_AQUA + "Note", Particle.NOTE);
        CosmeticsGui.registerParticle(new ItemStack(Material.REDSTONE), ChatColor.RED + "Redstone", Particle.REDSTONE);
        CosmeticsGui.registerParticle(new ItemStack(Material.SLIME_BALL), ChatColor.GREEN + "Slime", Particle.SLIME);
        CosmeticsGui.registerParticle(new ItemStack(Material.STAINED_GLASS, 1, (short)8), ChatColor.GRAY + "Fumée", Particle.SMOKE_LARGE);
    }
}
