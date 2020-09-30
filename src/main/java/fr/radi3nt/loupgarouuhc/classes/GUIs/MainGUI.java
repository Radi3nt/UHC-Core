package fr.radi3nt.loupgarouuhc.classes.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainGUI {

    static String MainGuiName = ChatColor.AQUA + "" + ChatColor.BOLD + "Config UHC";

    public static Inventory createGUI(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 9, MainGuiName);



        inventory.setItem(2, createStartItem());
        inventory.setItem(4, createOptionItem());
        inventory.setItem(6, createManageItem());

        return inventory;
    }

    public static ItemStack createStartItem() {
        ItemStack startItem = new ItemStack(Material.ARROW);
        ItemMeta startMeta = startItem.getItemMeta();
        startMeta.setDisplayName(ChatColor.GOLD + "Start");
        startMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        startItem.setItemMeta(startMeta);
        return startItem;
    }

    public static ItemStack createOptionItem () {
        ItemStack optionsItem = new ItemStack(Material.REDSTONE);
        ItemMeta optionsMeta = optionsItem.getItemMeta();
        optionsMeta.setDisplayName(ChatColor.GREEN + "Options");
        optionsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        optionsItem.setItemMeta(optionsMeta);
        return optionsItem;
    }

    public static ItemStack createManageItem() {
        ItemStack manageItem = new ItemStack(Material.COMPASS);
        ItemMeta manageMeta = manageItem.getItemMeta();
        manageMeta.setDisplayName(ChatColor.DARK_RED + "⚠ Manage ⚠");
        manageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        manageItem.setItemMeta(manageMeta);
        return manageItem;
    }



    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTitle().equals(MainGuiName);
    }

}
