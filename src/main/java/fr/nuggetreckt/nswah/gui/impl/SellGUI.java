package fr.nuggetreckt.nswah.gui.impl;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.auction.CategoryType;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import fr.nuggetreckt.nswah.util.MessageManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SellGUI implements CustomInventory {

    private final AuctionHouse instance;

    public final HashMap<Player, ItemStack> selectedItem;
    public final HashMap<Player, Long> itemPrice;

    public SellGUI(AuctionHouse instance) {
        this.instance = instance;
        this.selectedItem = new HashMap<>();
        this.itemPrice = new HashMap<>();
    }

    @Override
    public String getName() {
        return "§8§l»§r §3HDV §8§l«§r §8(§fVendre§8)";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public Supplier<ItemStack[]> getContents(@NotNull Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int itemCount = instance.getDatabaseManager().getRequestSender().getPlayerAuctionItemsCount(player);

        //Item
        if (selectedItem.containsKey(player) && selectedItem.get(player) != null) {
            slots[4] = selectedItem.get(player);
            if (itemPrice.containsKey(player) && itemPrice.get(player) != null) {
                slots[13] = new ItemUtils(Material.GREEN_STAINED_GLASS_PANE).setName("§8§l»§r §3Vendre §8§l«").hideFlags().setLore(" ", "§8| §fConfirme la vente de votre item pour §3" + itemPrice.get(player) + "NSc").toItemStack();
            } else {
                slots[13] = new ItemUtils(Material.YELLOW_STAINED_GLASS_PANE).setName("§8§l»§r §3Définir un prix §8§l«").hideFlags().setLore(" ", "§8| §fCliquez pour définir un prix").toItemStack();
            }
        } else {
            slots[13] = new ItemUtils(Material.GRAY_STAINED_GLASS_PANE).setName("§8§l»§r §cAucun item sélectionné §8§l«").hideFlags().setLore(" ", "§8| §fSélectionnez l'item que vous voulez", "  §fvendre dans votre inventaire").toItemStack();
        }

        //Utils
        slots[21] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Retour §8§l«").hideFlags().setLore(" ", "§8| §fRetourne au menu principal").toItemStack();
        slots[22] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();
        slots[24] = new ItemUtils(Material.LANTERN).setName("§8§l»§r §3Infos §8§l«").hideFlags().setLore(" ", "§8| §3" + itemCount + "§8/§3" + instance.getMaxSoldItems() + " §fitems en vente").toItemStack();

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
            case GRAY_STAINED_GLASS_PANE -> player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            case GREEN_STAINED_GLASS_PANE -> {
                CategoryType type = instance.getAuctionHandler().getCategoryTypeByItem(clickedItem);
                int itemCount = instance.getDatabaseManager().getRequestSender().getPlayerAuctionItemsCount(player);
                AuctionItem auctionItem;

                if (itemCount >= instance.getMaxSoldItems()) {
                    player.closeInventory();
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    player.sendMessage(String.format(MessageManager.TOO_MUCH_ITEMS.getMessage(), instance.getMaxSoldItems()));
                    instance.getGuiManager().open(player, AuctionHouseGUI.class);
                    return;
                }
                auctionItem = new AuctionItem(selectedItem.get(player), type, itemPrice.get(player), player);

                instance.getDatabaseManager().getRequestSender().insertAuctionItem(auctionItem);

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                player.sendMessage(MessageManager.ITEM_PUT_ON_SALE.getMessage());
                player.getInventory().removeItem(selectedItem.get(player));
                player.closeInventory();
            }
            case YELLOW_STAINED_GLASS_PANE -> {
                SignGUI signGUI = buildSignGUI(player);

                player.closeInventory();
                signGUI.open(player);
            }
            default -> {
                if (!isClickable(clickedItem)) return;
                if (!player.getInventory().equals(inventory)) return;

                if (!selectedItem.containsKey(player) || selectedItem.get(player) == null) {
                    selectedItem.put(player, clickedItem);
                } else {
                    selectedItem.replace(player, clickedItem);
                }
                instance.getGuiManager().refresh(player, this.getClass());
            }
        }
    }

    private boolean isClickable(@NotNull ItemStack clickedItem) {
        if (clickedItem.getType() == Material.AIR) return false;
        return clickedItem.getType() != Material.LIGHT_BLUE_STAINED_GLASS_PANE && !Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(" ");
    }

    private SignGUI buildSignGUI(Player player) {
        return SignGUI.builder()
                .setLines("§8§l\\/ \\/ \\/ \\/ \\/", null, "§8§l/\\ /\\ /\\ /\\ /\\")
                .setType(Material.BIRCH_SIGN)
                .setHandler((p, result) -> {
                    String line = result.getLine(1);

                    if (line.isEmpty() || !line.matches("[0-9]+")) {
                        return List.of(SignGUIAction.displayNewLines("§8§l\\/ \\/ \\/ \\/ \\/", null, "§8§l/\\ /\\ /\\ /\\ /\\", "§cValeur incorrecte"));
                    }
                    return List.of(
                            SignGUIAction.runSync(instance, () -> {
                                if (itemPrice.containsKey(player)) {
                                    itemPrice.replace(player, Long.parseLong(line));
                                } else {
                                    itemPrice.put(player, Long.parseLong(line));
                                }
                                player.sendMessage(String.format(MessageManager.ITEM_PRICE_SET.getMessage(), itemPrice.get(player)));
                                instance.getGuiManager().open(player, this.getClass());
                            })
                    );
                })
                .build();
    }
}
