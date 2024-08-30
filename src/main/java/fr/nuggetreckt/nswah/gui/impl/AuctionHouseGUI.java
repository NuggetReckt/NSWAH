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

public class AuctionHouseGUI implements CustomInventory {

    private final AuctionHouse instance;

    private final HashMap<Player, Integer> currentPage;
    private final HashMap<Integer, AuctionItem> auctionItems;
    private final int maxPerPage;

    private int pageCount = 1;

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
        setupAuctionItems(player);

        //AuctionItems
        for (AuctionItem auctionItem : auctionItems.values()) {
            ItemStack item = auctionItem.getItem();
            ItemMeta meta = item.getItemMeta();

            assert meta != null;
            meta.setLore(List.of(" ", " §8| §fVendu par : §3" + auctionItem.getSeller().getName(), " §8| §fPrix : §3" + auctionItem.getPrice() + "NSc"));

            slots[slot] = auctionItem.getItem();
            slots[slot].setItemMeta(meta);
            slots[slot].setAmount(item.getAmount());
            slot++;
        }

        //Utils
        if (currentPage.get(player) > 0) {
            slots[48] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Page précédente §8§l«").hideFlags().setLore(" ", "§8| §fRetour à la page précédente").toItemStack();
        } else {
            slots[48] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        }
        if (currentPage.get(player) < pageCount - 1) {
            slots[50] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Page suivante §8§l«").hideFlags().setLore(" ", "§8| §fAller à la page suivante").toItemStack();
        } else {
            slots[50] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        }
        slots[45] = new ItemUtils(Material.SUNFLOWER).setName("§8§l»§r §3Vendre §8§l«").hideFlags().setLore(" ", "§8| §fAccède au menu de vente").toItemStack();
        slots[46] = new ItemUtils(Material.NETHER_STAR).setName("§8§l»§r §3Mes items en vente §8§l«").hideFlags().setLore(" ", "§8| §fVoir les items que vous avez mis en vente").toItemStack();
        slots[49] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();
        slots[52] = new ItemUtils(Material.HOPPER).setName("§8§l»§r §3Trier §8§l«").hideFlags().setLore(" ", "§8| §fTrié par " + instance.getAuctionHandler().getCurrentSortType(player).getDisplayName()).toItemStack();
        slots[53] = new ItemUtils(Material.SNOWBALL).setName("§8§l»§r §3Rafraîchir §8§l«").hideFlags().setLore(" ", "§8| §fActualise la page").toItemStack();

        //Placeholders
        slots[47] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();
        slots[51] = new ItemUtils(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").toItemStack();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, @NotNull ItemStack clickedItem, int slot, boolean isLeftClick) {
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
                player.playSound(player, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1);
            }
            case SNOWBALL -> {
                instance.getGuiManager().refresh(player, this.getClass());
                player.playSound(player, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1);
            }
            case HOPPER -> {
                instance.getAuctionHandler().toggleTypeSort(player);
                instance.getGuiManager().refresh(player, this.getClass());
                player.playSound(player, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1);
            }
            case SUNFLOWER -> {
                player.closeInventory();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                instance.getGuiManager().open(player, SellGUI.class);
            }
            case NETHER_STAR -> {
                player.closeInventory();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                instance.getGuiManager().open(player, SoldItemsGUI.class);
            }
            case ARROW -> {
                int newPage = currentPage.get(player);

                if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("suivante")) {
                    newPage = newPage + 1;
                } else {
                    newPage = newPage - 1;
                }
                setCurrentPage(player, newPage, true);
                instance.getGuiManager().refresh(player, this.getClass());
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            }
            case LANTERN -> {
            }
            default -> {
                if (!isClickable(clickedItem)) return;
                if (player.getInventory().equals(inventory)) return;

                AuctionItem auctionItem = auctionItems.get(slot);
                ItemMeta meta = auctionItem.getItem().getItemMeta();
                BuyGUI buyGUI = (BuyGUI) instance.getGuiManager().registeredMenus.get(BuyGUI.class);
                EditGUI editGUI = (EditGUI) instance.getGuiManager().registeredMenus.get(EditGUI.class);

                assert meta != null;
                meta.setLore(null);
                auctionItem.getItem().setItemMeta(meta);
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                if (Objects.requireNonNull(auctionItem.getSeller().getName()).equalsIgnoreCase(player.getName())) {
                    editGUI.selectedItem.put(player, auctionItem);
                    instance.getGuiManager().open(player, EditGUI.class);
                    return;
                }
                buyGUI.selectedItem.put(player, auctionItem);
                instance.getGuiManager().open(player, BuyGUI.class);
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

    private void setupAuctionItems(Player player) {
        List<AuctionItem> items = instance.getAuctionHandler().getAuctionItems(player);
        int slot;
        int startIndex = currentPage.get(player) * maxPerPage;
        int endIndex = Math.min(startIndex + maxPerPage, items.size());

        auctionItems.clear();
        pageCount = (int) Math.ceil((double) items.size() / maxPerPage);

        for (int i = startIndex; i < endIndex; i++) {
            slot = i - startIndex;
            auctionItems.put(slot, items.get(i));
        }
    }
}
