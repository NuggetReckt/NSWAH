package fr.nuggetreckt.nswah.database;

import fr.noskillworld.api.utils.Credentials;
import fr.nuggetreckt.nswah.AuctionHouse;

public class DatabaseManager {

    private final Connector connector;
    private final RequestHandler requestsHandler;
    private final RequestSender requestSender;

    public DatabaseManager(AuctionHouse instance, Credentials credentials) {
        this.connector = new Connector(instance, credentials);
        this.requestsHandler = new RequestHandler(instance);
        this.requestSender = new RequestSender(instance);
    }

    public Connector getConnector() {
        return this.connector;
    }

    public RequestHandler getRequestHandler() {
        return this.requestsHandler;
    }

    public RequestSender getRequestSender() {
        return this.requestSender;
    }
}
