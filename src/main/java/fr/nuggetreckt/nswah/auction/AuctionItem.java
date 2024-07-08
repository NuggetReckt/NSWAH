package fr.nuggetreckt.nswah.auction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionItem {

    private long price;
    private final ItemStack item;
    private final CategoryType categoryType;
    private final Player seller;

    public AuctionItem(ItemStack item, CategoryType type, long price, Player seller) {
        this.price = price;
        this.item = item;
        this.categoryType = type;
        this.seller = seller;
    }

    public long getPrice() {
        return this.price;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Player getSeller() {
        return this.seller;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public CategoryType getCategory() {
        return categoryType;
    }
}
