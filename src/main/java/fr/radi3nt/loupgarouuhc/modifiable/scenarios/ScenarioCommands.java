package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ScenarioCommands implements CommandExecutor {

    static int i = 0;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Scenario.callCommand(commandSender, command, s, strings);
        if (strings[0].equalsIgnoreCase("scenario")) {
            if (commandSender instanceof Player) {
                if (LGPlayer.thePlayer((Player) commandSender).isInGame()) {
                    ((Player) commandSender).openInventory(createInventory((InventoryHolder) commandSender)[0]);
                }
            }
        }
        if (strings[0].equalsIgnoreCase("+")) {
            i++;
        }
        if (strings[0].equalsIgnoreCase("-")) {
            i--;
        }
        System.out.println(i);
        return true;
    }

    public Inventory[] createInventory(InventoryHolder owner) {
        int inventoryListSize = 4096;
        Inventory[] inventories = new Inventory[inventoryListSize];
        String name = ChatColor.GOLD + "Scenarios %page%";
        int size = 9 * 6;
        int number = 9 * 2;
        int currentPage = i; //0
        while (inventoryListSize > currentPage) {
            if (getScenariosItems(size - number, currentPage * (size - number)).isEmpty())
                break;

            inventories[currentPage] = Bukkit.createInventory(owner, size, name.replace("%page%", String.valueOf(currentPage + 1)));
            for (int i = 0; i < getScenariosItems(size - number, currentPage * (size - number)).size(); i++) {
                inventories[currentPage].setItem(i, getScenariosItems(size - number, currentPage * (size - number)).get(i));
            }
            addScroll(inventories[currentPage], currentPage);
            currentPage++;
        }
        return inventories;
    }

    private Inventory addScroll(Inventory inventory, int page) {
        int maxSize = inventory.getSize();
        ItemStack plus = new ItemStack(Material.PAPER);
        ItemStack sign = new ItemStack(Material.SIGN);
        ItemStack less = new ItemStack(Material.PAPER);
        ItemMeta plusMeta = plus.getItemMeta();
        ItemMeta lessMeta = less.getItemMeta();
        ItemMeta signMeta = sign.getItemMeta();
        plusMeta.setDisplayName(ChatColor.GREEN + "+");
        lessMeta.setDisplayName(ChatColor.GREEN + "-");
        signMeta.setDisplayName(ChatColor.RED + String.valueOf(page + 1));
        plus.setItemMeta(plusMeta);
        less.setItemMeta(lessMeta);
        sign.setItemMeta(signMeta);
        inventory.setItem(maxSize - 6, less);
        inventory.setItem(maxSize - 5, sign);
        inventory.setItem(maxSize - 4, plus);

        return inventory;
    }

    private ArrayList<ItemStack> getScenariosItems(int number, int offset) {
        int i = 0;
        int iOffset = 0;
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (Class<?> aClass : Scenario.getScenariosClasses()) {
            if (i >= number + offset)
                break;

            iOffset++;
            if (iOffset < offset)
                continue;

            ItemStack itemStack = null;
            try {
                itemStack = (ItemStack) aClass.getMethod("getItem").invoke(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
            ItemMeta meta = itemStack.getItemMeta();
            try {
                meta.setDisplayName((String) aClass.getMethod("getName").invoke(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
            itemStack.setItemMeta(meta);
            itemStacks.add(itemStack);
            i++;
        }
        return itemStacks;
    }

}
