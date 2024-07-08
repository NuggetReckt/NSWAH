package fr.nuggetreckt.nswah.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public interface CustomInventory {
    String getName();

    int getRows();

    default int getSlots() {
        return getRows() * 9;
    }

    Supplier<ItemStack[]> getContents(Player player);

    void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, boolean isLeftClick);
}
