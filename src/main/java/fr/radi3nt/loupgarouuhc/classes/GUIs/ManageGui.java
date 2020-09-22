package fr.radi3nt.loupgarouuhc.classes.GUIs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.loupgarouuhc.classes.lang.Language;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;

import java.lang.reflect.Field;
import java.util.UUID;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.langWarperInstance;

public class ManageGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > " + ChatColor.BOLD + "Manage";

    public static Inventory createGUI(Player player) {
        Language language = langWarperInstance.language;
        Inventory inventory = Bukkit.createInventory(player, 9, MainGuiName);

        inventory.setItem(0, createGameItem());
        inventory.setItem(1, createPlayerItem());
        inventory.setItem(8, createBackItem());

        return inventory;
    }

    public static ItemStack createPlayerItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        try {
            itemStack.setType(Material.PLAYER_HEAD);
        } catch (NoSuchFieldError error) {
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
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setDisplayName("Players");
        meta.setOwner("Red_white_200"); //TODO CHANGE MY PSEUDO !
        itemStack.setItemMeta(meta);
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
