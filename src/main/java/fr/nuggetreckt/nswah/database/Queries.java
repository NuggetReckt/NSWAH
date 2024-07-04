package fr.nuggetreckt.nswah.database;

public enum Queries {

    //Create table req
    CREATE_AUCTIONS_TABLE("""
            CREATE TABLE IF NOT EXISTS auction_items
            (
                id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                sellerUUID VARCHAR(36) NOT NULL,
                sellerName VARCHAR(50) NOT NULL,
                price BIGINT,
                date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                itemData TEXT NOT NULL
            );
            """)
    ;

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
