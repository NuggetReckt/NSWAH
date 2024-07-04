package fr.nuggetreckt.nswah.database;

import fr.nuggetreckt.nswah.AuctionHouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequestHandler {

    private final AuctionHouse instance;

    public Statement statement;
    public ResultSet resultSet;

    public RequestHandler(AuctionHouse instance) {
        this.instance = instance;
        this.statement = null;
        this.resultSet = null;
    }

    public void retrieveData(String query) {
        if (!isConnected()) {
            instance.getDatabaseManager().getConnector().connect();
        }

        try {
            statement = instance.getDatabaseManager().getConnector().getConn().createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        }
    }

    public void updateData(String query) {
        if (!isConnected()) {
            instance.getDatabaseManager().getConnector().connect();
        }

        try {
            statement = instance.getDatabaseManager().getConnector().getConn().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        }
    }

    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            } // ignore
            resultSet = null;
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignored) {
            } // ignore
            statement = null;
        }
    }

    private boolean isConnected() {
        return instance.getDatabaseManager().getConnector().isConnected();
    }
}
