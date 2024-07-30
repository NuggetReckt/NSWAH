package fr.nuggetreckt.nswah.auction;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class AuctionItem {

    private final int id;
    private long price;
    private final ItemStack item;
    private final CategoryType categoryType;
    private final OfflinePlayer seller;

    public AuctionItem(int id, ItemStack item, CategoryType type, long price, OfflinePlayer seller) {
        this.id = id;
        this.item = item;
        this.categoryType = type;
        this.price = price;
        this.seller = seller;
    }

    public AuctionItem(ItemStack item, CategoryType type, long price, OfflinePlayer seller) {
        this.id = 0;
        this.item = item;
        this.categoryType = type;
        this.price = price;
        this.seller = seller;
    }

    public int getId() {
        return this.id;
    }

    public long getPrice() {
        return this.price;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public OfflinePlayer getSeller() {
        return this.seller;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public CategoryType getCategory() {
        return categoryType;
    }
}
