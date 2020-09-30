package fr.radi3nt.loupgarouuhc.classes.GUIs;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;
import static org.bukkit.Bukkit.broadcastMessage;

public class RoleConfigGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > Options > " + ChatColor.BOLD + "Roles";

    public static Inventory createGUI(Player player, Integer page) {
        LGPlayer lgp = LGPlayer.thePlayer(player);

        ArrayList<Role> roles = new ArrayList<>();
        ArrayList<RoleSort> rolesSorts = new ArrayList<>();
        ArrayList<ItemStack> items = new ArrayList<>();


        try {
            for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet())
                for (int i = 0; i < roleNumber.getOrDefault(role.getKey(), 0); i++) {
                    roles.add(role.getValue().newInstance(new LGGame(parameters)));
                }
        } catch (Exception err) {
            broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
            err.printStackTrace();
        }

        for (Role role : roles) {
            rolesSorts.add(role.getRoleSort());
        }

        for (RoleSort roleSort : RoleSort.values()) {
            ItemStack rolesItem = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta rolesMeta = rolesItem.getItemMeta();
            if (rolesSorts.contains(roleSort)) {
                rolesItem.setType(Material.EMERALD_BLOCK);
                rolesMeta.setDisplayName(ChatColor.DARK_GREEN + roleSort.getName(lgp.getLanguage()));
                int i = 0;
                for (RoleSort rolesSort : rolesSorts) {
                    if (rolesSort == roleSort) {
                        i++;
                    }
                }
                rolesItem.setAmount(i);
            } else {
                rolesMeta.setDisplayName(ChatColor.DARK_RED + roleSort.getName(lgp.getLanguage()));
            }
            rolesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            rolesItem.setItemMeta(rolesMeta);

            items.add(rolesItem);
        }


        for (int i = 0; i < items.size()/(4*9)+1; i++) {
            if (page==i) {
                Inventory inventory = Bukkit.createInventory(player, 9*6, MainGuiName + " " + (i+1) + "/" + (items.size()/(4*9)+1));
                for (int u = 0; u < items.size(); u++) {
                    if (u>(4*9)*i-1 && u<(4*9)*(i+1)-1) {
                        inventory.addItem(items.get(u));
                    }
                }
                inventory.setItem(47, createBackItem());
                inventory.setItem(49, createBackPageItem());
                inventory.setItem(50, createNextPageItem());
                return inventory;
            }
        }
        return null;
    }

    public static Inventory createGUI(Player player, Integer page, Inventory inventory) {
        LGPlayer lgp = LGPlayer.thePlayer(player);


        ArrayList<Role> roles = new ArrayList<>();
        ArrayList<RoleSort> rolesSorts = new ArrayList<>();
        ArrayList<ItemStack> items = new ArrayList<>();


        try {
            for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet())
                for (int i = 0; i < roleNumber.getOrDefault(role.getKey(), 0); i++) {
                    roles.add(role.getValue().newInstance(new LGGame(parameters)));
                }
        } catch (Exception err) {
            broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
            err.printStackTrace();
        }

        for (Role role : roles) {
            rolesSorts.add(role.getRoleSort());
        }

        for (RoleSort roleSort : RoleSort.values()) {
            ItemStack rolesItem = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta rolesMeta = rolesItem.getItemMeta();
            if (rolesSorts.contains(roleSort)) {
                rolesItem.setType(Material.EMERALD_BLOCK);
                rolesMeta.setDisplayName(ChatColor.DARK_GREEN + roleSort.getName(lgp.getLanguage()));
                int i = 0;
                for (RoleSort rolesSort : rolesSorts) {
                    if (rolesSort == roleSort) {
                        i++;
                    }
                }
                rolesItem.setAmount(i);
            } else {
                rolesMeta.setDisplayName(ChatColor.DARK_RED + roleSort.getName(lgp.getLanguage()));
            }
            rolesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            rolesItem.setItemMeta(rolesMeta);

            items.add(rolesItem);
        }


        for (int i = 0; i < items.size()/(4*9)+1; i++) {
            if (page==i) {
                inventory.clear();
                for (int u = 0; u < items.size(); u++) {
                    if (u>(4*9)*i-1 && u<(4*9)*(i+1)-1) {
                        inventory.addItem(items.get(u));
                    }
                }
                inventory.setItem(47, createBackItem());
                inventory.setItem(49, createBackPageItem());
                inventory.setItem(50, createNextPageItem());
                return inventory;
            }
        }
        return null;
    }

    public static ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Back to options");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static ItemStack createNextPageItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Next");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static ItemStack createBackPageItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Back");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return item;
    }


    public static Integer getPage(InventoryView inventory) {
        if (checkInventoryView(inventory)) {
            char[] chars = new char[10];
            inventory.getTitle().getChars(MainGuiName.length(), inventory.getTitle().length()-String.valueOf(RoleSort.values().length/(4*9)+1).length()-1, chars, 0);
            String page = "";
            for (char charactere : chars) {
                page = page + charactere;
            }
            return Integer.valueOf(page.trim());
        }
        return null;
    }

    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTitle().contains(MainGuiName);
    }

}
