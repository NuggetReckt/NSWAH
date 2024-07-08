package fr.nuggetreckt.nswah.auction;

import fr.nuggetreckt.nswah.AuctionHouse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;

public class AuctionHandler {

    private final AuctionHouse instance;

    public final HashMap<Integer, AuctionItem> auctionItems;

    public AuctionHandler(AuctionHouse instance) {
        this.instance = instance;
        this.auctionItems = new HashMap<>();
    }

    public HashMap<Integer, AuctionItem> getAuctionItemsByCategory(CategoryType category) {
        HashMap<Integer, AuctionItem> items = new HashMap<>();
        AuctionItem auctionItem;

        for (int key : auctionItems.keySet()) {
            auctionItem = auctionItems.get(key);
            if (auctionItem.getCategory().equals(category)) {
                items.put(key, auctionItem);
            }
        }
        return items;
    }

    public CategoryType getCategoryTypeByItem(ItemStack item) {
        //Catégorisation des items
        return CategoryType.OTHER;
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
