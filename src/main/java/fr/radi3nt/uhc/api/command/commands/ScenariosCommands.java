package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScenariosCommands extends CommandArg {

    private static final int inventoryListSize = 1024;
    private static final HashMap<InventoryHolder, Inventory[]> inventories = new HashMap<>();

    public static Inventory[] createInventory(InventoryHolder owner, List<Scenario> activatedScenarios) {
        if (inventories.get(owner) == null) {
            Inventory[] inventory = new Inventory[inventoryListSize];
            String name = ChatColor.GOLD + "Scenarios %page%";
            int size = 9 * 6;
            int number = 9 * 2;
            int currentPage = 0;
            while (inventoryListSize > currentPage) {
                int offset = currentPage * (size - number);
                if (getScenariosItems(size - number, offset, activatedScenarios).isEmpty())
                    break;

                if (inventory[currentPage] == null)
                    inventory[currentPage] = Bukkit.createInventory(owner, size, name.replace("%page%", String.valueOf(currentPage + 1)));
                inventory[currentPage].clear();
                for (int i = 0; i < getScenariosItems(size - number, offset, activatedScenarios).size(); i++) {
                    inventory[currentPage].setItem(i, getScenariosItems(size - number, offset, activatedScenarios).get(i));
                }
                addScroll(inventory[currentPage], currentPage);
                currentPage++;
            }
            inventories.put(owner, inventory);
        }
        return inventories.get(owner);
    }

    public static Inventory[] updateInventory(InventoryHolder owner, List<Scenario> activatedScenarios) {
        if (inventories.get(owner) != null) {
            Inventory[] inventory = new Inventory[inventoryListSize];
            String name = ChatColor.GOLD + "Scenarios %page%";
            int size = 9 * 6;
            int number = 9 * 2;
            int currentPage = 0;
            while (inventoryListSize > currentPage) {
                int offset = currentPage * (size - number);
                if (getScenariosItems(size - number, offset, activatedScenarios).isEmpty())
                    break;


                inventory[currentPage] = Bukkit.createInventory(owner, size, name.replace("%page%", String.valueOf(currentPage + 1)));
                inventory[currentPage].clear();
                for (int i = 0; i < getScenariosItems(size - number, offset, activatedScenarios).size(); i++) {
                    inventory[currentPage].setItem(i, getScenariosItems(size - number, offset, activatedScenarios).get(i));
                }
                addScroll(inventory[currentPage], currentPage);
                currentPage++;
            }
            inventories.put(owner, inventory);
        } else {
            createInventory(owner, activatedScenarios);
        }
        return inventories.get(owner);
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

    private static ArrayList<ItemStack> getScenariosItems(int number, int offset, List<Scenario> activatedScenarios) {
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
                    if (activatedScenario.isActive())
                        meta.setDisplayName(ChatColor.GREEN + ChatColor.stripColor(meta.getDisplayName()));
                    else
                        meta.setDisplayName(ChatColor.GOLD + ChatColor.stripColor(meta.getDisplayName()));
                }
            }
            itemStack.setItemMeta(meta);
            itemStacks.add(itemStack);
            i++;
        }
        ArrayList<String> itemName = new ArrayList<>();
        for (ItemStack item : itemStacks) {
            itemName.add(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
        }
        Collections.sort(itemName);
        ArrayList<ItemStack> finalItem = new ArrayList<>();
        for (String s : itemName) {
            for (ItemStack item : itemStacks) {
                if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(s)) {
                    finalItem.add(item);
                }
            }
        }
        return finalItem;
    }

    @Override
    public void onCommand(CommandUtilis utilis) {
        if (utilis.checkIfPlayer()) {
            if (UHCPlayer.thePlayer((Player) utilis.getSender()).isInGame()) {
                updateInventory((InventoryHolder) utilis.getSender(), UHCPlayer.thePlayer((Player) utilis.getSender()).getGameData().getGame().getScenarios());
                ((Player) utilis.getSender()).openInventory(createInventory((InventoryHolder) utilis.getSender(), UHCPlayer.thePlayer((Player) utilis.getSender()).getGameData().getGame().getScenarios())[0]);
            } else {
                updateInventory((InventoryHolder) utilis.getSender(), UHCCore.getGames().get(0).getScenarios());
                if (!UHCCore.getGames().isEmpty())
                    ((Player) utilis.getSender()).openInventory(createInventory((InventoryHolder) utilis.getSender(), UHCCore.getGames().get(0).getScenarios())[0]);
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandUtilis utilis) {
        return null;
    }
}
