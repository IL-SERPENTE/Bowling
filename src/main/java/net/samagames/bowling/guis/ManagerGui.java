package net.samagames.bowling.guis;

import net.samagames.api.gui.AbstractGui;
import net.samagames.bowling.Bowling;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Rigner for project Bowling.
 */
public class ManagerGui extends AbstractGui
{
    private Bowling bowling;
    private boolean exit;

    public ManagerGui(Bowling bowling)
    {
        this(bowling, false);
    }

    public ManagerGui(Bowling bowling, boolean exit)
    {
        this.bowling = bowling;
        this.exit = exit;
    }

    @Override
    public void update(Player player)
    {
        if (!this.exit)
        {
            if (this.bowling.isPractice())
                this.setSlotData(ChatColor.DARK_AQUA + "Jouer", new ItemStack(Material.FIREBALL), 11, null, "play");
            this.setSlotData(ChatColor.AQUA + "Cosmétiques", new ItemStack(Material.NETHER_STAR), 15, null, "cosmetics");
        }
        else
            this.setSlotData(ChatColor.RED + "Quitter", new ItemStack(Material.BARRIER), 13, null, "quit");
        this.setSlotData(ChatColor.GREEN + "Retour", new ItemStack(Material.EMERALD), 31, null, "back");
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.bowling.getServer().createInventory(null, 45, this.bowling.getGame().getGameName());
        this.update(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        switch (action)
        {
            case "play":
                this.bowling.getGame().playPlayer(player);
                break ;
            case "back":
                player.closeInventory();
                break ;
            case "quit":
                if (!this.bowling.isPractice())
                    this.bowling.getSamaGamesAPI().getGameManager().kickPlayer(player, null);
                else
                {
                    this.bowling.getGame().stumpPlayer(player);
                    player.sendMessage(ChatColor.YELLOW + "Vous avez quitté la partie.");
                    player.closeInventory();
                }
                break ;
            case "cosmetics":
                this.bowling.getSamaGamesAPI().getGuiManager().openGui(player, new CosmeticsGui(this.bowling));
            default:
                break ;
        }
    }
}
