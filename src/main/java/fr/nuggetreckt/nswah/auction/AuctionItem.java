package fr.nuggetreckt.nswah.auction;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class AuctionItem {

    private long price;
    private final Item item;
    private final CategoryType categoryType;
    private final Player seller;

    public AuctionItem(long price, Item item, CategoryType type, Player seller) {
        this.price = price;
        this.item = item;
        this.categoryType = type;
        this.seller = seller;
    }

    public long getPrice() {
        return this.price;
    }

    public Item getItem() {
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
