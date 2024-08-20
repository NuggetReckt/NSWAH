package fr.nuggetreckt.nswah.database;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.auction.AuctionItem;
import fr.nuggetreckt.nswah.auction.SortType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public List<AuctionItem> getAuctionItems(@NotNull SortType sortType) {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        InputStream inputStream;
        int itemId;
        long itemPrice;
        UUID playerUUID;
        ItemStack item;
        List<AuctionItem> result = new ArrayList<>();

        requestsHandler.retrieveData(getQueriesBySortType(sortType).getQuery());
        try {
            while (requestsHandler.resultSet.next()) {
                inputStream = requestsHandler.resultSet.getBlob("itemData").getBinaryStream();
                itemId = requestsHandler.resultSet.getInt("id");
                itemPrice = requestsHandler.resultSet.getLong("price");
                playerUUID = UUID.fromString(requestsHandler.resultSet.getString("sellerUUID"));
                item = instance.getAuctionHandler().deserializeItem(inputStream);
                result.add(new AuctionItem(itemId, item, instance.getAuctionHandler().getCategoryTypeByItem(item), itemPrice, Bukkit.getOfflinePlayer(playerUUID)));
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

    public int getPlayerAuctionItemsCount(@NotNull Player player) {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();
        requestsHandler.retrieveData(String.format(Queries.RETRIEVE_PLAYER_AUCTIONS_ITEMS_COUNT.getQuery(), player.getUniqueId()));

        int count = 0;
        try {
            while (requestsHandler.resultSet.next()) {
                count = requestsHandler.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        }
        return count;
    }

    public void updateAuctionItemPrice(@NotNull AuctionItem item, long price) {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        requestsHandler.updateData(String.format(Queries.UPDATE_PRICE.getQuery(), price, item.getId()));
        requestsHandler.close();
    }

    public void deleteAuctionItem(@NotNull AuctionItem item) {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        requestsHandler.updateData(String.format(Queries.DELETE_AUCTION_ITEM.getQuery(), item.getId()));
        requestsHandler.close();
    }

    public void createAuctionsTable() {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        requestsHandler.updateData(Queries.CREATE_AUCTIONS_TABLE.getQuery());
        requestsHandler.close();
    }

    private Queries getQueriesBySortType(@NotNull SortType sortType) {
        Queries query = null;

        switch (sortType) {
            case DATE_DESC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_DATE_DESC;
            case DATE_ASC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_DATE;
            case PLAYER_ASC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_PLAYER_NAME;
            case PLAYER_DESC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_PLAYER_NAME_DESC;
            case PRICE_ASC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_PRICE;
            case PRICE_DESC -> query = Queries.RETRIEVE_AUCTION_ITEMS_BY_PRICE_DESC;
        }
        return query;
    }
}
