package fr.nuggetreckt.nswah.gui;

import fr.nuggetreckt.nswah.AuctionHouse;
import fr.nuggetreckt.nswah.gui.impl.AuctionHouseGUI;
import fr.nuggetreckt.nswah.gui.impl.BuyGUI;
import fr.nuggetreckt.nswah.gui.impl.EditGUI;
import fr.nuggetreckt.nswah.gui.impl.SellGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private final AuctionHouse instance;

    public GuiManager(AuctionHouse instance) {
        this.instance = instance;
        this.registersGUI();
    }

    public final Map<Class<? extends CustomInventory>, CustomInventory> registeredMenus = new HashMap<>();

    public void open(Player player, Class<? extends CustomInventory> gClass) {
        if (!registeredMenus.containsKey(gClass)) return;

        CustomInventory menu = registeredMenus.get(gClass);
        Inventory inv = Bukkit.createInventory(null, menu.getSlots(), menu.getName());
        inv.setContents(menu.getContents(player).get());
        player.openInventory(inv);
    }

    public void refresh(Player player, Class<? extends CustomInventory> gClass) {
        if (!registeredMenus.containsKey(gClass)) return;

        CustomInventory menu = registeredMenus.get(gClass);
        Inventory inv = player.getOpenInventory().getTopInventory();
        inv.setContents(menu.getContents(player).get());
    }

    private void addMenu(CustomInventory m) {
        registeredMenus.put(m.getClass(), m);
    }

    private void registersGUI() {
        addMenu(new AuctionHouseGUI(instance));
        addMenu(new SellGUI(instance));
        addMenu(new BuyGUI(instance));
        addMenu(new EditGUI(instance));
    }
}
