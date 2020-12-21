package fr.radi3nt.uhc.api.scenarios;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ScenarioData {

    public static final int MAX_CHAR_BEFORE_LINE_RETURN = 20;

    private final String name;
    private String description;
    private ItemStack itemStack = new ItemStack(Material.BARRIER);

    public ScenarioData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&', description);
    }

    public ScenarioData setDescription(String description) {
        this.description = ChatColor.translateAlternateColorCodes('&', description);
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ScenarioData setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
}
