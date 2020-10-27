package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioUtilis;
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


    public static Inventory[] createInventory(InventoryHolder owner, ArrayList<Scenario> activatedScenarios) {
        int inventoryListSize = 4096;
        Inventory[] inventories = new Inventory[inventoryListSize];
        String name = ChatColor.GOLD + "Scenarios %page%";
        int size = 9 * 6;
        int number = 9 * 2;
        int currentPage = 0;
        while (inventoryListSize > currentPage) {
            int offset = currentPage * (size - number);
            if (getScenariosItems(size - number, offset, activatedScenarios).isEmpty())
                break;

            inventories[currentPage] = Bukkit.createInventory(owner, size, name.replace("%page%", String.valueOf(currentPage + 1)));
            for (int i = 0; i < getScenariosItems(size - number, offset, activatedScenarios).size(); i++) {
                inventories[currentPage].setItem(i, getScenariosItems(size - number, offset, activatedScenarios).get(i));
            }
            addScroll(inventories[currentPage], currentPage);
            currentPage++;
        }
        return inventories;
    }

    private static Inventory addScroll(Inventory inventory, int page) {
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

    private static ArrayList<ItemStack> getScenariosItems(int number, int offset, ArrayList<Scenario> activatedScenarios) {
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
            meta.setDisplayName(ChatColor.RED + meta.getDisplayName());
            for (Scenario activatedScenario : activatedScenarios) {
                if (activatedScenario.getClass() == aClass) {
                    meta.setDisplayName(ChatColor.GREEN + ChatColor.stripColor(meta.getDisplayName()));
                }
            }
            itemStack.setItemMeta(meta);
            itemStacks.add(itemStack);
            i++;
        }
        return itemStacks;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ScenarioUtilis.callCommand(commandSender, command, s, strings);
        if (strings[0].equalsIgnoreCase("scenario")) {
            if (commandSender instanceof Player) {
                if (LGPlayer.thePlayer((Player) commandSender).isInGame()) {
                    ((Player) commandSender).openInventory(createInventory((InventoryHolder) commandSender, LGPlayer.thePlayer((Player) commandSender).getGameData().getGame().getScenarios())[0]);
                } else {
                    ((Player) commandSender).openInventory(createInventory((InventoryHolder) commandSender, LoupGarouUHC.getGameInstance().getScenarios())[0]);
                }
            }
        }
        return true;
    }

}
