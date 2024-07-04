package fr.nuggetreckt.nswah.database;

import fr.noskillworld.api.utils.Credentials;
import fr.nuggetreckt.nswah.AuctionHouse;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private final AuctionHouse instance;

    private Connection conn;

    private final String user;
    private final String password;
    private final String name;

    public Connector(AuctionHouse instance, @NotNull Credentials credentials) {
        this.instance = instance;
        this.user = credentials.getDBUser();
        this.password = credentials.getDBPassword();
        this.name = credentials.getDBName();

        connect();
    }

    public void connect() {
        if (user == null || password == null || name == null) {
            instance.getLogger().severe("Credentials cannot be empty! Aborting connection.");
            return;
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + name, user, password);
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        }
    }

    public boolean isConnected() {
        try {
            return conn.isValid(1000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            instance.getLogger().severe("SQLException: " + e.getMessage());
            instance.getLogger().severe("SQLState: " + e.getSQLState());
            instance.getLogger().severe("VendorError: " + e.getErrorCode());
        }
    }

    public Connection getConn() {
        return conn;
    }
}