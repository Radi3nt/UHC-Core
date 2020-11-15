package fr.radi3nt.loupgarouuhc.classes.GUIs;

import fr.radi3nt.uhc.api.lang.lang.Languages;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getRoleNumber;
import static org.bukkit.ChatColor.getByChar;

public class RoleConfigGui {

    static String MainGuiName = ChatColor.AQUA + "Config UHC > Options > " + ChatColor.BOLD + "Roles";

    public enum PageType {
        VILAGER("Villager", Material.WHEAT),
        SOLO("Solo", Material.MAGMA_CREAM),
        LOUPGAROU("Loup garou", Material.POISONOUS_POTATO);

        private final String name;
        private final Material material;

        PageType(String name, Material material) {
            this.name = name;
            this.material = material;
        }

        public String getName() {
            return name;
        }

        public Material getMaterial() {
            return material;
        }
    }

    public static Inventory createGUI(Player player, Integer page, PageType type) {
        UHCPlayer lgp = UHCPlayer.thePlayer(player);

        List<ItemStack> items = getItems(lgp.getLanguage());
        boolean lgS = false;
        boolean soloS = false;
        boolean villS = true;

        RoleType roleType = RoleType.VILLAGER;
        switch (type) {
            case SOLO:
                roleType=RoleType.NEUTRAL;
                villS = false;
                soloS=true;
                break;

            case LOUPGAROU:
                roleType=RoleType.LOUP_GAROU;
                villS = false;
                lgS=true;
                break;
        }

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            boolean isSolo = false;
            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> roleIdentityConstructorEntry : Role.getRoleLinkByStringKey().entrySet()) {
                if (roleIdentityConstructorEntry.getKey().getRoleType() == roleType) {
                    if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(roleIdentityConstructorEntry.getKey().getName(lgp.getLanguage()))) {
                        isSolo=true;
                    }
                }
            }
            if (!isSolo) {
                items.remove(item);
                i--;
            }
        }


        for (int i = 0; i < items.size()/(4*9)+1; i++) {
            if (page==i) {
                Inventory inventory = Bukkit.createInventory(player, 9*6, MainGuiName + " " + (i+1) + "/" + (items.size()/(4*9)+1));
                for (int u = 0; u < items.size(); u++) {
                    if (u>(4*9)*i-1 && u<(4*9)*(i+1)-1) {
                        inventory.addItem(items.get(u));
                    }
                }
                inventory.setItem(46, createBackItem());
                inventory.setItem(48, createBackPageItem());
                inventory.setItem(49, createNextPageItem());
                inventory.setItem(51, createRoleItem(PageType.VILAGER.getName(), PageType.VILAGER.getMaterial(), villS));
                inventory.setItem(52, createRoleItem(PageType.SOLO.getName(), PageType.SOLO.getMaterial(), soloS));
                inventory.setItem(53, createRoleItem(PageType.LOUPGAROU.getName(), PageType.LOUPGAROU.getMaterial(), lgS));
                return inventory;
            }
        }
        return null;
    }

    private static List<ItemStack> getItems(Languages languages) {
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
                rolesMeta.setDisplayName(ChatColor.DARK_GREEN + roleSort.getName(languages));
                int i = 0;
                for (RoleIdentity rolesSort : rolesSorts) {
                    if (rolesSort == roleSort) {
                        i++;
                    }
                }
                rolesItem.setAmount(i);
            } else {
                rolesMeta.setDisplayName(ChatColor.DARK_RED + roleSort.getName(languages));
            }
            rolesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            rolesItem.setItemMeta(rolesMeta);

            items.add(rolesItem);
        }

        ArrayList<String> itemName = new ArrayList<>();
        for (ItemStack item : items) {
            itemName.add(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
        }
        Collections.sort(itemName);
        ArrayList<ItemStack> finalItem = new ArrayList<>();
        for (String s : itemName) {
            for (ItemStack item : items) {
                if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(s)) {
                    finalItem.add(item);
                }
            }
        }

        return finalItem;
    }

    public static Inventory createGUI(Player player, Integer page, Inventory inventory, PageType type) {
        UHCPlayer lgp = UHCPlayer.thePlayer(player);


        List<ItemStack> items = getItems(lgp.getLanguage());

        boolean lgS = false;
        boolean soloS = false;
        boolean villS = true;

        RoleType roleType = RoleType.VILLAGER;
        switch (type) {
            case SOLO:
                roleType=RoleType.NEUTRAL;
                villS = false;
                soloS=true;
                break;

            case LOUPGAROU:
                roleType=RoleType.LOUP_GAROU;
                villS = false;
                lgS=true;
                break;
        }

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            boolean isSolo = false;
            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> roleIdentityConstructorEntry : Role.getRoleLinkByStringKey().entrySet()) {
                if (roleIdentityConstructorEntry.getKey().getRoleType() == roleType) {
                    if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(roleIdentityConstructorEntry.getKey().getName(lgp.getLanguage()))) {
                        isSolo=true;
                    }
                }
            }
            if (!isSolo) {
                items.remove(item);
                i--;
            }
        }

        for (int i = 0; i < items.size()/(4*9)+1; i++) {
            if (page==i) {
                inventory.clear();
                for (int u = 0; u < items.size(); u++) {
                    if (u>(4*9)*i-1 && u<(4*9)*(i+1)-1) {
                        inventory.addItem(items.get(u));
                    }
                }
                inventory.setItem(46, createBackItem());
                inventory.setItem(48, createBackPageItem());
                inventory.setItem(49, createNextPageItem());
                inventory.setItem(51, createRoleItem(PageType.VILAGER.getName(), PageType.VILAGER.getMaterial(), villS));
                inventory.setItem(52, createRoleItem(PageType.SOLO.getName(), PageType.SOLO.getMaterial(), soloS));
                inventory.setItem(53, createRoleItem(PageType.LOUPGAROU.getName(), PageType.LOUPGAROU.getMaterial(), lgS));
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

    public static ItemStack createRoleItem(String name, Material material, boolean selected) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (selected)
            name=ChatColor.GREEN + name;
        else
            name=ChatColor.RED + name;
        itemMeta.setDisplayName(name);
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

    public static PageType getPageType(InventoryView inventory) {
        if (checkInventoryView(inventory)) {
            ItemStack solo = inventory.getTopInventory().getItem(52);
            ItemStack lg = inventory.getTopInventory().getItem(53);

            if (getLastColor(solo.getItemMeta().getDisplayName()).equals(ChatColor.GREEN)) {
                return PageType.SOLO;
            }
            if (getLastColor(lg.getItemMeta().getDisplayName()).equals(ChatColor.GREEN)) {
                return PageType.LOUPGAROU;
            }
            return PageType.VILAGER;
        }
        return null;
    }

    private static ChatColor getLastColor(String input) {
        int length = input.length();
        for(int index = length - 1; index > -1; --index) {
            char section = input.charAt(index);
            if (section == 167 && index < length - 1) {
                char c = input.charAt(index + 1);
                return getByChar(c);
            }
        }
        return null;
    }

    public static boolean checkInventoryView(InventoryView inventory) {
        return inventory.getTopInventory().getTitle().contains(MainGuiName);
    }

}
