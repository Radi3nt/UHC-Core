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

public class ManageGameGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > " + "Manage >" +  ChatColor.BOLD + "Game";

    public static Inventory createGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, MainGuiName);

        inventory.setItem(0, createSkipItem());
        inventory.setItem(8, createBackItem());

        return inventory;
    }

    public static ItemStack createSkipItem() {
        ItemStack rolesItem = new ItemStack(Material.BARRIER);
        try {
            rolesItem.setType(Material.getMaterial("PLAYER_HEAD"));
        } catch (NoClassDefFoundError error) {
            try {
                rolesItem = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 0, (byte) 3);
            } catch (NoClassDefFoundError error1) {
                try {
                    rolesItem = new ItemStack(Material.getMaterial("SKULL"), 1, (short) 0, (byte) 3);
                } catch (NoClassDefFoundError error2) {
                    return rolesItem;
                }
            }
        }

        SkullMeta rolesMeta = (SkullMeta) rolesItem.getItemMeta();
        rolesMeta.setDisplayName(ChatColor.GOLD + "Skip day");
        rolesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        rolesMeta.setOwner("MHF_ArrowRight");
        rolesItem.setItemMeta(rolesMeta);
        return rolesItem;
    }



    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTitle().equals(MainGuiName);
    }

    public static ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Back to manage");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }

}
