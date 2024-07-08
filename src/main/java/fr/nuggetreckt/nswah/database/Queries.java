package fr.nuggetreckt.nswah.database;

public enum Queries {
    INSERT_AUCTION_ITEM("INSERT INTO auction_items (sellerUUID, sellerName, price, itemData) VALUES (?, ?, ?, ?);"),
    RETRIEVE_AUCTION_ITEMS("SELECT * FROM auction_items;"),

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
