package fr.nuggetreckt.nswah.database;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class RequestSender {

    private final AuctionHouse instance;

    private RequestHandler requestsHandler;

    public RequestSender(AuctionHouse instance) {
        this.instance = instance;
    }

    public void insertAuctionItem(@NotNull AuctionItem item) {
        PreparedStatement statement;
        ByteArrayInputStream inputStream;

        try {
            statement = instance.getDatabaseManager().getConnector().getConn().prepareStatement(Queries.INSERT_AUCTION_ITEM.getQuery());
            inputStream = instance.getAuctionHandler().serializeItem(item.getItem());

            statement.setString(1, item.getSeller().getUniqueId().toString());
            statement.setString(2, item.getSeller().getName());
            statement.setLong(3, item.getPrice());
            statement.setBlob(4, inputStream);
            statement.executeUpdate();
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, AuctionItem> getAuctionItems() {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        InputStream inputStream;
        int itemId;
        long itemPrice;
        UUID playerUUID;
        ItemStack item;
        HashMap<Integer, AuctionItem> result = new HashMap<>();

        requestsHandler.retrieveData(Queries.RETRIEVE_AUCTION_ITEMS.getQuery());
        try {
            while (requestsHandler.resultSet.next()) {
                System.out.println("DEBUG");
                inputStream = requestsHandler.resultSet.getBlob("itemData").getBinaryStream();
                System.out.println("DEBUG: " + inputStream);
                itemId = requestsHandler.resultSet.getInt("id");
                itemPrice = requestsHandler.resultSet.getLong("price");
                playerUUID = UUID.fromString(requestsHandler.resultSet.getString("sellerUUID"));
                item = instance.getAuctionHandler().deserializeItem(inputStream);
                result.put(itemId, new AuctionItem(item, instance.getAuctionHandler().getCategoryTypeByItem(item), itemPrice, Bukkit.getPlayer(playerUUID)));
            }
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            requestsHandler.close();
        }
        return result;
    }

    public void createAuctionsTable() {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        requestsHandler.updateData(Queries.CREATE_AUCTIONS_TABLE.getQuery());
        requestsHandler.close();
    }
}
