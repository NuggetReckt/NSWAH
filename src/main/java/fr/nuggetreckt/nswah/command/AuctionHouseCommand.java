package fr.nuggetreckt.nswah.command;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.gui.impl.AuctionHouseGUI;
import fr.nuggetreckt.nswah.gui.impl.EditGUI;
import fr.nuggetreckt.nswah.gui.impl.SellGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuctionHouseCommand implements CommandExecutor {

    private final AuctionHouse instance;

    public AuctionHouseCommand(AuctionHouse instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 0) {
                SellGUI sellGUI = (SellGUI) instance.getGuiManager().registeredMenus.get(SellGUI.class);
                EditGUI editGUI = (EditGUI) instance.getGuiManager().registeredMenus.get(EditGUI.class);

                sellGUI.selectedItem.remove(player);
                sellGUI.itemPrice.remove(player);
                editGUI.selectedItem.remove(player);
                instance.getGuiManager().open(player, AuctionHouseGUI.class);
                return true;
            }
        }
        return true;
    }
}
