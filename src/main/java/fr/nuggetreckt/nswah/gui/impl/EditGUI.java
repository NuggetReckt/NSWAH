package fr.nuggetreckt.nswah.gui.impl;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import fr.nuggetreckt.nswah.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class EditGUI implements CustomInventory {

    private final AuctionHouse instance;

    public final HashMap<Player, AuctionItem> selectedItem;

    public EditGUI(AuctionHouse instance) {
        this.instance = instance;
        this.selectedItem = new HashMap<>();
    }

    @Override
    public String getName() {
        return "§8§l»§r §3HDV §8§l«§r §8(§fEdit§8)";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        //Item
        if (selectedItem.containsKey(player) && selectedItem.get(player) != null) {
            slots[4] = selectedItem.get(player).getItem();
        } else {
            slots[13] = new ItemUtils(Material.GRAY_STAINED_GLASS_PANE).setName("§8§l»§r §cErreur §8§l«").hideFlags().setLore(" ", "§8| §fUne erreur est survenue", "  §flors du chargement de l'item").toItemStack();
        }

        //Utils
        slots[14] = new ItemUtils(Material.REDSTONE).setName("§8§l»§r §3Supprimer §8§l«").hideFlags().setLore(" ", "§8| §fRetire l'item de la vente").toItemStack();
        slots[12] = new ItemUtils(Material.SUNFLOWER).setName("§8§l»§r §3Changer le prix §8§l«").hideFlags().setLore(" ", "§8| §fModifie le prix de vente de l'item", "§8| §fActuel : §3" + selectedItem.get(player).getPrice() + "NSc").toItemStack();
        slots[21] = new ItemUtils(Material.ARROW).setName("§8§l»§r §3Retour §8§l«").hideFlags().setLore(" ", "§8| §fRetourne au menu principal").toItemStack();
        slots[22] = new ItemUtils(Material.BARRIER).setName("§8§l»§r §3Fermer §8§l«").hideFlags().setLore(" ", "§8| §fFerme le menu").toItemStack();

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
            case REDSTONE -> {
                if (getItemCount(player) > 35) {
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    player.sendMessage(MessageManager.NO_INVENTORY_ROOM.getMessage());
                    return;
                }
                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> instance.getDatabaseManager().getRequestSender().deleteAuctionItem(selectedItem.get(player)));
                player.getInventory().addItem(selectedItem.get(player).getItem());
                player.sendMessage(MessageManager.ITEM_REMOVED.getMessage());
                player.closeInventory();
            }
            case SUNFLOWER -> {
                SignGUI signGUI = buildSignGUI(player);

                player.closeInventory();
                signGUI.open(player);
            }
        }
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
                            SignGUIAction.run(() -> {
                                instance.getDatabaseManager().getRequestSender().updateAuctionItemPrice(selectedItem.get(player), Long.parseLong(line));
                                selectedItem.get(player).setPrice(Long.parseLong(line));
                                player.sendMessage(MessageManager.ITEM_PRICE_UPDATED.getMessage());
                            }),
                            SignGUIAction.runSync(instance, () -> instance.getGuiManager().open(player, this.getClass()))
                    );
                })
                .build();
    }

    private int getItemCount(@NotNull Player player) {
        int itemCount = 0;

        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && !i.getType().isAir()) {
                itemCount++;
            }
        }
        return itemCount;
    }
}
