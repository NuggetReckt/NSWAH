package fr.nuggetreckt.nswah.command;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.gui.impl.AuctionHouseGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuctionHouseCommand implements CommandExecutor {

    private final AuctionHouse instance;

    public AuctionHouseCommand(AuctionHouse instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 0) {
                instance.getGuiManager().open(player, AuctionHouseGUI.class);
                return true;
            }
            if (args[0].equalsIgnoreCase("test")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                AuctionItem auctionItem = new AuctionItem(0, item, instance.getAuctionHandler().getCategoryTypeByItem(item), Integer.parseInt(args[1]), player);
                instance.getDatabaseManager().getRequestSender().insertAuctionItem(auctionItem);
                player.getInventory().getItemInMainHand().setType(Material.AIR);
            } else if (args[0].equalsIgnoreCase("test2")) {
                List<AuctionItem> auctionItems = instance.getDatabaseManager().getRequestSender().getAuctionItems();

                for (AuctionItem item : auctionItems) {
                    player.sendMessage("DEBUG: item = " + item.getItem().getType());
                    player.sendMessage("DEBUG: seller = " + item.getSeller());
                    player.sendMessage("DEBUG: price = " + item.getPrice());

                    player.getInventory().addItem(item.getItem());
                }
            }
        }
        return true;
    }
}
