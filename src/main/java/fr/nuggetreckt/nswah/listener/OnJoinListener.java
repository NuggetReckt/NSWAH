package fr.nuggetreckt.nswah.listener;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionHandler;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.util.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class OnJoinListener implements Listener {

    private final AuctionHouse instance;

    public OnJoinListener(AuctionHouse instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuctionHandler auctionHandler = instance.getAuctionHandler();

        if (auctionHandler.offlinePlayerSoldItems.containsKey(player)) {
            for (AuctionItem item : auctionHandler.offlinePlayerSoldItems.get(player)) {
                player.sendMessage(String.format(MessageManager.OFFLINE_PAYMENT_RECEIVED.getMessage(), item.getBuyer().getName(), item.getPrice()));
            }
            auctionHandler.offlinePlayerSoldItems.remove(player);
        }
    }
}
