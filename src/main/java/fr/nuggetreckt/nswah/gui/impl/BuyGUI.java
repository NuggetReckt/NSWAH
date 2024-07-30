package fr.nuggetreckt.nswah.gui.impl;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.gui.CustomInventory;
import fr.nuggetreckt.nswah.util.ItemUtils;
import fr.nuggetreckt.nswah.util.MessageManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Supplier;

public class BuyGUI implements CustomInventory {

    private final AuctionHouse instance;

    public final HashMap<Player, AuctionItem> selectedItem;

    public BuyGUI(AuctionHouse instance) {
        this.instance = instance;
        this.selectedItem = new HashMap<>();
    }

    @Override
    public String getName() {
        return "§8§l»§r §3HDV §8§l«§r §8(§fAcheter§8)";
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
        slots[13] = new ItemUtils(Material.GREEN_STAINED_GLASS_PANE).setName("§8§l»§r §3Confirmer §8§l«").hideFlags().setLore(" ", "§8| §fConfirmer l'achat de l'item").toItemStack();
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
            case GREEN_STAINED_GLASS_PANE -> {
                AuctionItem auctionItem = selectedItem.get(player);
                Economy economy = instance.getEconomy();

                if (getItemCount(player) > 35) {
                    player.sendMessage(MessageManager.NO_INVENTORY_ROOM.getMessage());
                    return;
                }
                if (economy.getBalance(player) < auctionItem.getPrice()) {
                    player.sendMessage(MessageManager.NO_ENOUGH_MONEY.getMessage());
                    return;
                }
                instance.getDatabaseManager().getRequestSender().deleteAuctionItem(auctionItem);
                player.getInventory().addItem(auctionItem.getItem());
                economy.withdrawPlayer(player, auctionItem.getPrice());
                economy.depositPlayer(auctionItem.getSeller(), auctionItem.getPrice());
                player.sendMessage(String.format(MessageManager.PAYMENT_SUCCESS.getMessage(), auctionItem.getPrice(), auctionItem.getSeller().getName()));
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 5);
                player.closeInventory();

                if (auctionItem.getSeller().isOnline()) {
                    Player target = (Player) auctionItem.getSeller();
                    target.sendMessage(String.format(MessageManager.PAYMENT_RECEIVED.getMessage(), player.getName(), auctionItem.getPrice()));
                    target.playSound(target, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 5);
                }
            }
        }
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
