package net.samagames.bowling.guis;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.gui.AbstractGui;
import net.samagames.api.shops.IPlayerShop;
import net.samagames.bowling.Bowling;
import net.samagames.bowling.entities.Ball;
import net.samagames.bowling.entities.StandBall;
import net.samagames.bowling.game.BPlayer;
import net.samagames.tools.GlowEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Rigner for project Bowling.
 */
public class CosmeticsGui extends AbstractGui
{
    public static final List<ParticleWrapper> PARTICLES = new ArrayList<>();

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
            this.setSlotData(ChatColor.GREEN + "Léger (3 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.LIGHT ? GlowEffect.addGlow(new ItemStack(Material.SLIME_BALL)) : new ItemStack(Material.SLIME_BALL), 11, CosmeticsGui.prepareDescription(163, player.getUniqueId()), "light");
            this.setSlotData(ChatColor.DARK_GREEN + "Moyen (5 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.MEDIUM ? GlowEffect.addGlow(new ItemStack(Material.SNOW_BALL)) : new ItemStack(Material.SNOW_BALL), 13, CosmeticsGui.prepareDescription(164, player.getUniqueId()), "medium");
            this.setSlotData(ChatColor.DARK_GRAY + "Lourd (7 kg)", bPlayer.getBallDescription().getBallWeight() == StandBall.BallWeight.HEAVY ? GlowEffect.addGlow(new ItemStack(Material.FIREBALL)) : new ItemStack(Material.FIREBALL), 15, CosmeticsGui.prepareDescription(165, player.getUniqueId()), "heavy");

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
            if (bPlayer != null && ballWeight != null)
            {
                try
                {
                    IPlayerShop iPlayerShop = this.plugin.getSamaGamesAPI().getShopsManager().getPlayer(bPlayer.getUUID());
                    if (iPlayerShop.getTransactionsByID(ballWeight.getItemId()) != null)
                    {
                        for (StandBall.BallWeight tmp : StandBall.BallWeight.values())
                            if (iPlayerShop.getTransactionsByID(tmp.getItemId()) != null && iPlayerShop.isSelectedItem(tmp.getItemId()))
                                iPlayerShop.setSelectedItem(tmp.getItemId(), false);
                        iPlayerShop.setSelectedItem(ballWeight.getItemId(), true);
                        bPlayer.getBallDescription().setBallWeight(ballWeight);
                        player.sendMessage(ChatColor.YELLOW + "La masse de votre boule a bien été changée.");
                    }
                } catch (Exception ignored) {}
                this.update(player);
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
                this.setSlotData(StandBall.WOOL_NAMES[i], bPlayer.getBallDescription().getBallColor() == i ? GlowEffect.addGlow(new ItemStack(Material.WOOL, 1, i)) : new ItemStack(Material.WOOL, 1, i), i + (i > 13 ? 14 : i > 6 ? 12 : 10), CosmeticsGui.prepareDescription(i + 147, player.getUniqueId()), "color");
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
                        try
                        {
                            IPlayerShop iPlayerShop = this.plugin.getSamaGamesAPI().getShopsManager().getPlayer(bPlayer.getUUID());
                            for (int i = 0; i < 16; i++)
                            {
                                if (i == stack.getDurability() && iPlayerShop.getTransactionsByID(i + 147) != null)
                                {
                                    bPlayer.getBallDescription().setBallColor(stack.getDurability());
                                    player.sendMessage(ChatColor.YELLOW + "La couleur de votre boule a bien été changée.");
                                    iPlayerShop.setSelectedItem(i + 147, true);
                                }
                                else if (i != stack.getDurability() && iPlayerShop.getTransactionsByID(i + 147) != null && iPlayerShop.isSelectedItem(i + 147))
                                    iPlayerShop.setSelectedItem(i + 147, false);
                            }
                        } catch (Exception ignored) {}
                    }
                    this.update(player);
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
            for (ParticleWrapper particlePair : CosmeticsGui.PARTICLES)
            {
                this.setSlotData(particlePair.name, particlePair.particle == bPlayer.getBallDescription().getParticle() ? GlowEffect.addGlow(particlePair.itemStack.clone()) : particlePair.itemStack.clone(), i + (i > 13 ? 14 : i > 6 ? 12 : 10), prepareDescription(particlePair.itemId, player.getUniqueId()), "particle;" + particlePair.particle);
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
                        try
                        {
                            IPlayerShop iPlayerShop = this.plugin.getSamaGamesAPI().getShopsManager().getPlayer(player.getUniqueId());
                            for (ParticleWrapper triple : CosmeticsGui.PARTICLES)
                                if (particle == triple.particle && iPlayerShop.getTransactionsByID(triple.itemId) != null)
                                {
                                    iPlayerShop.setSelectedItem(triple.itemId, true);
                                    bPlayer.getBallDescription().setParticle(particle);
                                    player.sendMessage(ChatColor.YELLOW + "Les particules de votre boule ont bien été changées.");
                                }
                                else if (iPlayerShop.getTransactionsByID(triple.itemId) != null && iPlayerShop.isSelectedItem(triple.itemId))
                                    iPlayerShop.setSelectedItem(triple.itemId, false);
                        } catch (Exception ignored) {}
                    }
                    this.update(player);
                    break ;
                case "back":
                    this.plugin.getSamaGamesAPI().getGuiManager().openGui(player, new CosmeticsGui(this.plugin));
                default:
                    break ;
            }
        }
    }

    private static String[] prepareDescription(int itemId, UUID uuid)
    {
        IPlayerShop iPlayerShop = SamaGamesAPI.get().getShopsManager().getPlayer(uuid);
        if (iPlayerShop.getTransactionsByID(itemId) != null)
            return new String[]{ ChatColor.GREEN + "Cliquez pour activer." };
        else
            return new String[]{ ChatColor.RED + "Vous ne possédez pas ce cosmétique. Acheteez le dans la boutique."};
    }

    private static void registerParticle(ItemStack itemStack, String name, Particle particle, int itemId)
    {
        CosmeticsGui.PARTICLES.add(new ParticleWrapper(itemStack, name, particle, itemId));
    }

    static
    {
        CosmeticsGui.registerParticle(new ItemStack(Material.RED_ROSE), ChatColor.RED + "Coeur", Particle.HEART, 166);
        CosmeticsGui.registerParticle(new ItemStack(Material.OBSIDIAN), ChatColor.DARK_PURPLE + "Portail", Particle.PORTAL, 167);
        CosmeticsGui.registerParticle(new ItemStack(Material.EMERALD), ChatColor.GREEN + "Emeraude", Particle.VILLAGER_HAPPY, 168);
        CosmeticsGui.registerParticle(new ItemStack(Material.END_ROD), ChatColor.WHITE + "End", Particle.END_ROD, 169);
        CosmeticsGui.registerParticle(new ItemStack(Material.FLINT_AND_STEEL), ChatColor.DARK_RED + "Flamme", Particle.FLAME, 170);
        CosmeticsGui.registerParticle(new ItemStack(Material.LAVA_BUCKET), ChatColor.DARK_RED + "Lave", Particle.LAVA, 171);
        CosmeticsGui.registerParticle(new ItemStack(Material.JUKEBOX), ChatColor.DARK_AQUA + "Note", Particle.NOTE, 172);
        CosmeticsGui.registerParticle(new ItemStack(Material.REDSTONE), ChatColor.RED + "Redstone", Particle.REDSTONE, 173);
        CosmeticsGui.registerParticle(new ItemStack(Material.SLIME_BALL), ChatColor.GREEN + "Slime", Particle.SLIME, 174);
        CosmeticsGui.registerParticle(new ItemStack(Material.STAINED_GLASS, 1, (short)8), ChatColor.GRAY + "Fumée", Particle.SMOKE_LARGE, 175);
    }

    public static class ParticleWrapper
    {
        private ItemStack itemStack;
        private String name;
        private Particle particle;
        private int itemId;

        private ParticleWrapper(ItemStack itemStack, String name, Particle particle, int itemId)
        {
            this.itemStack = itemStack;
            this.name = name;
            this.particle = particle;
            this.itemId = itemId;
        }

        public int getItemId()
        {
            return itemId;
        }

        public Particle getParticle()
        {
            return particle;
        }
    }
}
