package fr.nuggetreckt.nswah.auction;

import fr.nuggetreckt.nswah.AuctionHouse;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class AuctionHandler {

    private final AuctionHouse instance;

    public final List<AuctionItem> auctionItems;

    public AuctionHandler(AuctionHouse instance) {
        this.instance = instance;
        this.auctionItems = new ArrayList<>();
    }

    public List<AuctionItem> getAuctionItemsByCategory(CategoryType category) {
        List<AuctionItem> items = new ArrayList<>();

        for (AuctionItem auctionItem : auctionItems) {
            if (auctionItem.getCategory().equals(category)) {
                items.add(auctionItem);
            }
        }
        return items;
    }

    public CategoryType getCategoryTypeByItem(Item item) {
        //Catégorisation des items
        return CategoryType.OTHER;
    }

    private String serializeAuctionItem(AuctionItem item) {
        //Serialization
        return null;
    }

    private AuctionItem deserializeAuctionItem(String string) {
        //Deserialization
        return null;
    }
}
