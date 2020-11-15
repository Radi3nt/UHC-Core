package fr.radi3nt.uhc.api.gui;

import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;

public abstract class GUI {

    private static final Set<Class<? extends GUI>> guis = new HashSet<>();

    public void registerGUI(Class<? extends GUI> gui) {
        guis.add(gui);
    }

    public abstract Inventory createInventory();

    public boolean checkInventory(Inventory inventory) {
        Inventory createdIventory = createInventory();
        if (createdIventory.getTitle().equals(inventory.getTitle()))
            return createdIventory.getSize() == inventory.getSize();

        return false;
    }

}
