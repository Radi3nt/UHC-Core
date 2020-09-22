package fr.radi3nt.loupgarouuhc.classes.GUIs;

import fr.radi3nt.loupgarouuhc.classes.lang.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.langWarperInstance;

public class OptionsItems {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > " + ChatColor.BOLD + "Options";

    public static Inventory createGUI(Player player) {
        Language language = langWarperInstance.language;
        Inventory inventory = Bukkit.createInventory(player, 9, MainGuiName);

        inventory.setItem(0, createRolesItem());
        inventory.setItem(8, createBackItem());

        return inventory;
    }

    public static ItemStack createRolesItem() {
        ItemStack rolesItem = new ItemStack(Material.ARROW);
        ItemMeta rolesMeta = rolesItem.getItemMeta();
        rolesMeta.setDisplayName(ChatColor.GOLD + "Roles");
        rolesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        rolesItem.setItemMeta(rolesMeta);
        return rolesItem;
    }


    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTitle().equals(MainGuiName);
    }

    public static ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Back to options");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }

}
