package fr.radi3nt.loupgarouuhc.classes.GUIs;

import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
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

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getRoleNumber;

public class RoleConfigGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > Options > " + ChatColor.BOLD + "Roles";

    public static Inventory createGUI(Player player, Integer page) {
        LGPlayer lgp = LGPlayer.thePlayer(player);

        ArrayList<RoleIdentity> rolesSorts = new ArrayList<>();
        ArrayList<ItemStack> items = new ArrayList<>();


        try {
            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> role : Role.getRoleLinkByStringKey().entrySet())
                for (int i = 0; i < getRoleNumber().getOrDefault(role.getKey().getId(), 0); i++) {
                    rolesSorts.add(role.getKey());
                }
        } catch (Exception err) {
            Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
            Logger.getGeneralLogger().log(err);
        }


        for (RoleIdentity roleSort : Role.getRoleLinkByStringKey().keySet()) {
            ItemStack rolesItem = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta rolesMeta = rolesItem.getItemMeta();
            if (rolesSorts.contains(roleSort)) {
                rolesItem.setType(Material.EMERALD_BLOCK);
                rolesMeta.setDisplayName(ChatColor.DARK_GREEN + roleSort.getName(lgp.getLanguage()));
                int i = 0;
                for (RoleIdentity rolesSort : rolesSorts) {
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


        ArrayList<RoleIdentity> rolesSorts = new ArrayList<>();
        ArrayList<ItemStack> items = new ArrayList<>();


        try {
            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> role : Role.getRoleLinkByStringKey().entrySet())
                for (int i = 0; i < getRoleNumber().getOrDefault(role.getKey().getId(), 0); i++) {
                    rolesSorts.add(role.getKey());
                }
        } catch (Exception err) {
            Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
            Logger.getGeneralLogger().log(err);
        }

        for (RoleIdentity roleSort : Role.getRoleLinkByStringKey().keySet()) {
            ItemStack rolesItem = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta rolesMeta = rolesItem.getItemMeta();
            if (rolesSorts.contains(roleSort)) {
                rolesItem.setType(Material.EMERALD_BLOCK);
                rolesMeta.setDisplayName(ChatColor.DARK_GREEN + roleSort.getName(lgp.getLanguage()));
                int i = 0;
                for (RoleIdentity rolesSort : rolesSorts) {
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
            inventory.getTitle().getChars(MainGuiName.length(), inventory.getTitle().length() - String.valueOf(Role.getRoleLinkByStringKey().size() / (4 * 9) + 1).length() - 1, chars, 0);
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
