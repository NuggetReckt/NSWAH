package fr.nuggetreckt.nswah;

import fr.noskillworld.api.utils.Credentials;
import fr.nuggetreckt.nswah.auction.AuctionHandler;
import fr.nuggetreckt.nswah.command.AuctionHouseCommand;
import fr.nuggetreckt.nswah.database.DatabaseManager;
import fr.nuggetreckt.nswah.gui.GuiManager;
import fr.nuggetreckt.nswah.listener.OnInvClickListener;
import fr.nuggetreckt.nswah.listener.OnInvExit;
import io.github.cdimascio.dotenv.Dotenv;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

public class AuctionHouse extends JavaPlugin {

    private final String prefix;

    private static AuctionHouse instance;

    private final DatabaseManager databaseManager;
    private final GuiManager guiManager;
    private final AuctionHandler auctionHandler;
    private Economy economy;

    private final Logger logger;

    private final int maxSoldItems = 5;

    public AuctionHouse() {
        instance = this;
        prefix = "§8[§3HDV§8] §r";
        logger = Logger.getLogger("Minecraft");

        databaseManager = new DatabaseManager(this, getCredentials());
        guiManager = new GuiManager(this);
        auctionHandler = new AuctionHandler(this);
    }

    @Override
    public void onEnable() {
        //create table if not exists
        databaseManager.getRequestSender().createAuctionsTable();

        //Register commands
        Objects.requireNonNull(this.getCommand("hdv")).setExecutor(new AuctionHouseCommand(this));

        //Register events
        getServer().getPluginManager().registerEvents(new OnInvClickListener(this), this);
        getServer().getPluginManager().registerEvents(new OnInvExit(this), this);

        //Set economy
        setEconomy();

        logger.info(String.format("[%s] Plugin loaded successfully", getDescription().getName()));
    }

    @Override
    public void onDisable() {
        databaseManager.getConnector().close();

        logger.info(String.format("[%s] Plugin shut down successfully", getDescription().getName()));
    }

    public static AuctionHouse getInstance() {
        return instance;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    public AuctionHandler getAuctionHandler() {
        return this.auctionHandler;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public int getMaxSoldItems() {
        return this.maxSoldItems;
    }

    private @NotNull Credentials getCredentials() {
        Dotenv dotenv = Dotenv.configure()
                .directory("/env/")
                .filename(".env")
                .load();

        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        String name = dotenv.get("DB_NAME");

        return new Credentials(user, password, name);
    }

    private void setEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logger.severe("The Vault plugin was not found! Please install it before starting the server.");
            logger.info("Stopping the server...");
            getServer().shutdown();
            return;
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            economy = provider.getProvider();
        }
    }
}
