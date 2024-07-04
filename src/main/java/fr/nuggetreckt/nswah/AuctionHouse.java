package fr.nuggetreckt.nswah;

import fr.nuggetreckt.nswah.command.AuctionHouseCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public class AuctionHouse extends JavaPlugin {

    private final String prefix;

    private static AuctionHouse instance;

    private final Logger logger;

    public AuctionHouse() {
        instance = this;
        prefix = "§8[§3HDV§8] §r";
        logger = Logger.getLogger("Minecraft");
    }

    @Override
    public void onEnable() {
        //Register commands
        Objects.requireNonNull(this.getCommand("hdv")).setExecutor(new AuctionHouseCommand());

        logger.info(String.format("[%s] Plugin loaded successfully", getDescription().getName()));
    }

    @Override
    public void onDisable() {
        logger.info(String.format("[%s] Plugin shut down successfully", getDescription().getName()));
    }

    public static AuctionHouse getInstance() {
        return instance;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
