package fr.nuggetreckt.nswah.auction;

import fr.nuggetreckt.nswah.AuctionHouse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionHandler {

    private final AuctionHouse instance;

    public AuctionHandler(AuctionHouse instance) {
        this.instance = instance;
    }

    public List<AuctionItem> getAuctionItems() {
        return instance.getDatabaseManager().getRequestSender().getAuctionItems();
    }

    public List<AuctionItem> getAuctionItemsByCategory(CategoryType category) {
        List<AuctionItem> auctionItems = getAuctionItems();
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
