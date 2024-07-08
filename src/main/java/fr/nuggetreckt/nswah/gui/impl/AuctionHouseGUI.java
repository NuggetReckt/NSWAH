package fr.nuggetreckt.nswah.gui.impl;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class AuctionHouseGUI implements CustomInventory {

    private final AuctionHouse instance;

    private int currentPage;

    public AuctionHouseGUI(AuctionHouse instance) {
        this.instance = instance;
    }

    @Override
    public String getName() {
        return "§8§l»§r §3HDV §8§l«§r §8(§fMenu§8)";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        currentPage = 1;

        //AuctionItems
        for (int i = 0; i < getSlots() - 9; i++) {
            //Do something
            slots[i] = new ItemUtils(Material.PUFFERFISH).setName("test").hideFlags().toItemStack();
        }

        //Utils
        slots[49] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();
        slots[53] = new ItemUtils(Material.SNOWBALL).setName("§8§l»§r §3Rafraîchir §8§l«").hideFlags().setLore(" ", "§8| §fActualise la page").toItemStack();

        //Placeholders
        slots[45] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[46] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[47] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[48] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[50] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[51] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[52] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, @NotNull ItemStack clickedItem, int slot, boolean isLeftClick) {
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
            case SNOWBALL -> instance.getGuiManager().refresh(player, this.getClass());
            default -> {
                if (!isClickable(clickedItem)) return;
                player.sendMessage("test");
            }
        }
    }

    private boolean isClickable(@NotNull ItemStack clickedItem) {
        if (clickedItem.getType() == Material.AIR) return false;
        return clickedItem.getType() != Material.LIGHT_BLUE_STAINED_GLASS_PANE && !Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(" ");
    }
}
