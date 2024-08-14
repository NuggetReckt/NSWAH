package fr.nuggetreckt.nswah.auction;

import fr.nuggetreckt.nswah.AuctionHouse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuctionHandler {

    private final AuctionHouse instance;

    public final HashMap<Player, SortType> sortTypeMap;

    public AuctionHandler(AuctionHouse instance) {
        this.instance = instance;
        this.sortTypeMap = new HashMap<>();
    }

    public List<AuctionItem> getAuctionItems(Player player) {
        sortTypeMap.putIfAbsent(player, SortType.DATE_DESC);
        return instance.getDatabaseManager().getRequestSender().getAuctionItems(getCurrentSortType(player));
    }

    public List<AuctionItem> getAuctionItemsByCategory(Player player, CategoryType category) {
        List<AuctionItem> auctionItems = getAuctionItems(player);
        List<AuctionItem> items = new ArrayList<>();

        for (AuctionItem auctionItem : auctionItems) {
            if (auctionItem.getCategory().equals(category)) {
                items.add(auctionItem);
            }
        }
        return items;
    }

    public CategoryType getCategoryTypeByItem(ItemStack item) {
        //Catégorisation des items
        return CategoryType.OTHER;
    }

    public SortType getCurrentSortType(@NotNull Player player) {
        return sortTypeMap.get(player);
    }

    public boolean exists(int id) {
        List<AuctionItem> auctionItems = instance.getDatabaseManager().getRequestSender().getAuctionItems(SortType.DATE_DESC);

        for (AuctionItem item : auctionItems) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void toggleTypeSort(@NotNull Player player) {
        SortType current = getCurrentSortType(player);
        SortType[] values = SortType.values();

        if (current == values[values.length - 1]) {
            sortTypeMap.replace(player, values[0]);
        } else {
            sortTypeMap.replace(player, values[current.ordinal() + 1]);
        }
    }

    public @NotNull ByteArrayInputStream serializeItem(@NotNull ItemStack item) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeObject(item);
        dataOutput.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ItemStack deserializeItem(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null || inputStream.available() == 0) {
            return null;
        }
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        ItemStack item = (ItemStack) dataInput.readObject();
        dataInput.close();

        return item;
    }
}
