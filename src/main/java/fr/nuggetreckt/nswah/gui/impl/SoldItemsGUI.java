package fr.nuggetreckt.nswah.gui.impl;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SoldItemsGUI implements CustomInventory {

    private final AuctionHouse instance;
    private final HashMap<Integer, AuctionItem> auctionItems;

    public SoldItemsGUI(AuctionHouse instance) {
        this.instance = instance;
        auctionItems = new HashMap<>();
    }

    @Override
    public String getName() {
        return "§8§l»§r §3HDV §8§l«§r §8(§fItems§8)";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        List<AuctionItem> soldItems = instance.getAuctionHandler().getPlayerSoldItems(player);
        int slot = 11;

        //Items
        auctionItems.clear();
        for (AuctionItem auctionItem : soldItems) {
            auctionItems.put(slot, auctionItem);

            ItemStack item = auctionItem.getItem();
            ItemMeta meta = item.getItemMeta();

            assert meta != null;
            meta.setLore(List.of(" ", " §8| §fPrix : §3" + auctionItem.getPrice() + "NSc", " ", "§8§l» §fClique pour éditer cet item"));

            slots[slot] = auctionItem.getItem();
            slots[slot].setItemMeta(meta);
            slots[slot].setAmount(item.getAmount());
            slot++;
        }

        //Utils
        slots[21] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Retour §8§l«").hideFlags().setLore(" ", "§8| §fRetourne au menu principal").toItemStack();
        slots[22] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();
        slots[24] = new ItemUtils(Material.LANTERN).setName("§8§l»§r §3Infos §8§l«").hideFlags().setLore(" ", "§8| §3" + soldItems.size() + "§8/§3" + instance.getMaxSoldItems() + " §fitems en vente").toItemStack();

        //Placeholders
        slots[0] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[1] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[7] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[8] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[9] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[17] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[18] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[19] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[25] = new ItemUtils(Material.BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[26] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, @NotNull ItemStack clickedItem, int slot, boolean isLeftClick) {
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
                player.playSound(player, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1);
            }
            case ARROW -> {
                player.closeInventory();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                instance.getGuiManager().open(player, AuctionHouseGUI.class);
            }
            case LANTERN -> {
            }
            default -> {
                if (!isClickable(clickedItem)) return;
                if (player.getInventory().equals(inventory)) return;

                AuctionItem item = auctionItems.get(slot);
                EditGUI editGUI = (EditGUI) instance.getGuiManager().registeredMenus.get(EditGUI.class);

                editGUI.selectedItem.put(player, item);
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                instance.getGuiManager().open(player, EditGUI.class);
            }
        }
    }

    private boolean isClickable(@NotNull ItemStack clickedItem) {
        if (clickedItem.getType() == Material.AIR) return false;
        return clickedItem.getType() != Material.LIGHT_BLUE_STAINED_GLASS_PANE && !Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(" ");
    }
}
