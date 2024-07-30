package fr.nuggetreckt.nswah.database;

public enum Queries {
    INSERT_AUCTION_ITEM("INSERT INTO auction_items (sellerUUID, sellerName, price, itemData) VALUES (?, ?, ?, ?);"),
    DELETE_AUCTION_ITEM("DELETE FROM auction_items WHERE id = %d;"),
    UPDATE_PRICE("UPDATE auction_items SET price = %d WHERE id = %d;"),
    RETRIEVE_AUCTION_ITEMS_BY_DATE("SELECT * FROM auction_items ORDER BY date;"),
    RETRIEVE_AUCTION_ITEMS_BY_DATE_DESC("SELECT * FROM auction_items ORDER BY date DESC;"),
    RETRIEVE_AUCTION_ITEMS_BY_PLAYER_NAME("SELECT * FROM auction_items ORDER BY sellerName;"),
    RETRIEVE_AUCTION_ITEMS_BY_PLAYER_NAME_DESC("SELECT * FROM auction_items ORDER BY sellerName DESC;"),
    RETRIEVE_AUCTION_ITEMS_BY_PRICE("SELECT * FROM auction_items ORDER BY price;"),
    RETRIEVE_AUCTION_ITEMS_BY_PRICE_DESC("SELECT * FROM auction_items ORDER BY price DESC;"),

    //Create table req
    CREATE_AUCTIONS_TABLE("""
            CREATE TABLE IF NOT EXISTS auction_items
            (
                id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                sellerUUID VARCHAR(36) NOT NULL,
                sellerName VARCHAR(50) NOT NULL,
                price BIGINT NOT NULL,
                date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                itemData BLOB NOT NULL
            );
            """);

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
