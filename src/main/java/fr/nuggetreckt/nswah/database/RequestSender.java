package fr.nuggetreckt.nswah.database;

import fr.nuggetreckt.nswah.AuctionHouse;

public class RequestSender {

    private final AuctionHouse instance;

    private RequestHandler requestsHandler;

    public RequestSender(AuctionHouse instance) {
        this.instance = instance;
    }

    public void createAuctionsTable() {
        requestsHandler = instance.getDatabaseManager().getRequestHandler();

        requestsHandler.updateData(Queries.CREATE_AUCTIONS_TABLE.getQuery());
        requestsHandler.close();
    }
}
