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
import org.bukkit.inventory.meta.SkullMeta;


public class ManageGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > " + ChatColor.BOLD + "Manage";

    public static Inventory createGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, MainGuiName);

        inventory.setItem(0, createGameItem());
        inventory.setItem(1, createPlayerItem());
        inventory.setItem(8, createBackItem());

        return inventory;
    }

    public static ItemStack createPlayerItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        try {
            itemStack = new ItemStack(Material.getMaterial("PLAYER_HEAD"));
        } catch (NoClassDefFoundError error) {
            try {
                itemStack = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);
            } catch (NoClassDefFoundError error1) {
                try {
                    itemStack = new ItemStack(Material.getMaterial("SKULL"), 1, (short) 0, (byte) 3);
                } catch (NoClassDefFoundError error2) {
                    return itemStack;
                }
            }
        }
        if (itemStack.getType()!=Material.BARRIER) {
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setDisplayName("Players");
            meta.setOwner("Red_white_200"); //TODO CHANGE MY PSEUDO !
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public static ItemStack createGameItem() {
        ItemStack itemStack = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Game");
        itemStack.setItemMeta(meta);
        return itemStack;
    }



    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTitle().equals(MainGuiName);
    }

    public static ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Back to main");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }

}
