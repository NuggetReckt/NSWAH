package fr.nuggetreckt.nswah.gui.impl;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class AuctionHouseGUI implements CustomInventory {

    private final AuctionHouse instance;

    private final HashMap<Player, Integer> currentPage;
    private final HashMap<Integer, AuctionItem> auctionItems;
    private final int maxPerPage;

    public AuctionHouseGUI(AuctionHouse instance) {
        this.instance = instance;
        this.currentPage = new HashMap<>();
        this.auctionItems = new HashMap<>();
        this.maxPerPage = getSlots() - 9;
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
        int slot = 0;

        setCurrentPage(player, 0, false);
        setupAuctionItems();

        //AuctionItems
        for (AuctionItem auctionItem : auctionItems.values()) {
            ItemStack item = auctionItem.getItem();

            assert item.getItemMeta() != null;
            slots[slot] = new ItemUtils(item.getType()).setName(item.getItemMeta().getDisplayName()).setLore(" ", " §8| §fVendu par : §3" + auctionItem.getSeller().getName(), " §8| §fPrix : §3" + auctionItem.getPrice()).addEnchantments(item.getEnchantments()).toItemStack();
            slot++;
        }

        //Utils
        if (currentPage.get(player) > 0) {
            slots[48] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Page précédente §8§l«").hideFlags().setLore(" ", "§8| §fRetour à la page précédente").toItemStack();
        } else {
            slots[48] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        }
        slots[49] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();
        slots[50] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Page suivante §8§l«").hideFlags().setLore(" ", "§8| §fAller à la page suivante").toItemStack();
        slots[53] = new ItemUtils(Material.SNOWBALL).setName("§8§l»§r §3Rafraîchir §8§l«").hideFlags().setLore(" ", "§8| §fActualise la page").toItemStack();

        //Placeholders
        slots[45] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[46] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[47] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[51] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[52] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, @NotNull ItemStack clickedItem, int slot, boolean isLeftClick) {
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
            case SNOWBALL -> instance.getGuiManager().refresh(player, this.getClass());
            case ARROW -> {
                int newPage = currentPage.get(player);

                if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("suivante")) {
                    newPage = newPage + 1;
                } else {
                    newPage = newPage - 1;
                }
                setCurrentPage(player, newPage, true);
                instance.getGuiManager().refresh(player, this.getClass());
            }
            default -> {
                if (!isClickable(clickedItem)) return;
                player.sendMessage("test");
                //Do something
            }
        }
    }

    private boolean isClickable(@NotNull ItemStack clickedItem) {
        if (clickedItem.getType() == Material.AIR) return false;
        return clickedItem.getType() != Material.LIGHT_BLUE_STAINED_GLASS_PANE && !Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(" ");
    }

    public void setCurrentPage(Player player, int page, boolean doOverride) {
        if (!currentPage.containsKey(player) || currentPage.get(player) == null) {
            currentPage.put(player, page);
        } else if (doOverride) {
            currentPage.replace(player, page);
        }
    }

    private void setupAuctionItems() {
        List<AuctionItem> items = instance.getAuctionHandler().getAuctionItems();
        int slot = 0;

        if (!auctionItems.isEmpty()) {
            auctionItems.clear();
        }
        //TODO: Items by page
        for (AuctionItem item : items) {
            if (slot >= maxPerPage) break;
            auctionItems.put(slot, item);
            slot++;
        }
    }
}
