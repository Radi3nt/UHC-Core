package fr.radi3nt.uhc.uhc.listeners.gui;

import fr.radi3nt.uhc.api.command.commands.ScenariosCommands;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.gui.guis.MainGUI;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ClickEvent implements Listener {

    @EventHandler
    public void OnClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null && e.getCurrentItem().getType()==Material.AIR) {
            if (MainGUI.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(MainGUI.createStartItem())) {
                    if (!UHCCore.getGameQueue().isEmpty())
                        UHCCore.getGameQueue().get(0).updateStart();
                }
            }
            if (e.getView().getTopInventory().getTitle().contains(ChatColor.GOLD + "Scenarios ")) {
                UHCGame game = UHCPlayer.thePlayer(player).getGameData().getGame();
                if (game == null || game.getState() == GameState.LOBBY) {
                    if (!UHCCore.getGameQueue().isEmpty())
                        game = UHCCore.getGameQueue().get(0);
                    else
                        return;
                }
                String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                int currentPage = Integer.parseInt(ChatColor.stripColor(e.getView().getTitle()).replace("Scenarios ", "").trim()) - 1;
                if (ChatColor.stripColor(itemName).equalsIgnoreCase("+")) {
                    System.out.println("ok");
                    currentPage+=1;
                }
                if (ChatColor.stripColor(itemName).equalsIgnoreCase("-")) {
                    currentPage-=1;
                }
                Class<?> aClass = null;
                for (Class<? extends Scenario> scenariosClass : Scenario.getRepertoriedScenariosClasses()) {
                    Method method = null;
                    try {
                        method = scenariosClass.getMethod("getData");
                    } catch (NoSuchMethodException noSuchMethodException) {
                        noSuchMethodException.printStackTrace();
                    }
                    if (method == null)
                        continue;
                    ScenarioData obj = null;
                    try {
                        obj = (ScenarioData) method.invoke(null);
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                    if (obj == null)
                        continue;

                    if (obj.getName().equals(itemName))
                        aClass = scenariosClass;
                }
                if (aClass != null) {
                    Scenario scenario = null;
                    try {
                        scenario = (Scenario) aClass.getConstructor(UHCGame.class).newInstance(game);
                    } catch (InstantiationException instantiationException) {
                        instantiationException.printStackTrace();
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    } catch (NoSuchMethodException noSuchMethodException) {
                        noSuchMethodException.printStackTrace();
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                    if (e.getClick().isLeftClick()) {
                        boolean value = false;
                        for (Scenario gameScenario : game.getScenarios()) {
                            if (gameScenario.getClass().equals(scenario.getClass())) {
                                value = true;
                                break;
                            }
                        }
                        if (!value) {
                            game.addScenario(scenario);
                            if (game.getState() == GameState.PLAYING) {
                                scenario.activate();
                            }
                        }
                    } else if (e.getClick().isRightClick()) {
                        for (int i = 0; i < game.getScenarios().size(); i++) {
                            Scenario gameScenario = game.getScenarios().get(i);
                            if (gameScenario.getClass().equals(scenario.getClass())) {
                                gameScenario.deactivate();
                                gameScenario.unregister();
                                game.getScenarios().remove(gameScenario);
                                i--;
                            }
                        }
                    } else if (e.getClick() == ClickType.MIDDLE) {
                        Inventory inventory = Bukkit.createInventory(player, 9 * 5, ChatColor.GOLD + "Options for " + itemName);
                        for (Scenario gameScenario : game.getScenarios()) {
                            if (gameScenario.getClass().equals(scenario.getClass())) {
                                scenario = gameScenario;
                            }
                        }
                        for (ScenarioGetter annotation : scenario.getScenarioGetAnnotations()) {
                            try {
                                ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
                                ItemMeta meta = itemStack.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + annotation.name());
                                Class<?> aClass1 = null;
                                ArrayList<String> lore = new ArrayList<>();
                                try {
                                    aClass1 = scenario.getScenarioGetMethod(annotation.name()).getReturnType();
                                } catch (NoSuchMethodException noSuchMethodException) {
                                    noSuchMethodException.printStackTrace();
                                }
                                if (aClass1 == Boolean.class || aClass1 == boolean.class) {
                                    itemStack.setType(Material.LEVER);
                                    lore.add(0, String.valueOf((boolean) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Integer.class || aClass1 == int.class) {
                                    itemStack.setType(Material.PAPER);
                                    lore.add(0, String.valueOf((int) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Double.class || aClass1 == double.class) {
                                    itemStack.setType(Material.EMPTY_MAP);
                                    lore.add(0, String.valueOf((double) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Float.class || aClass1 == float.class) {
                                    itemStack.setType(Material.MAP);
                                    lore.add(0, String.valueOf((float) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == List.class) {
                                    itemStack.setType(Material.BOOK);
                                } else {
                                    meta.setDisplayName(ChatColor.RED + "INVALID FIELD: " + aClass1.getTypeName());
                                }
                                meta.setLore(lore);
                                itemStack.setItemMeta(meta);
                                inventory.addItem(itemStack);
                            } catch (Exception err) {
                                Logger.getGeneralLogger().log(err);
                            }
                        }
                        player.openInventory(inventory);
                    }
                    if (e.getClick() != ClickType.MIDDLE) {
                        e.getWhoClicked().openInventory(ScenariosCommands.updateInventory(e.getWhoClicked(), game.getScenarios())[currentPage]);
                    }
                }
                e.setCancelled(true);
            } else if (e.getView().getTopInventory().getTitle().contains(ChatColor.GOLD + "Options for ") && e.getCurrentItem() != null) {
                if (e.getCurrentItem() != null) {
                    UHCGame game = UHCPlayer.thePlayer(player).getGameData().getGame();
                    if (game == null || game.getState() == GameState.LOBBY) {
                        if (!UHCCore.getGameQueue().isEmpty())
                            game = UHCCore.getGameQueue().get(0);
                        else return;
                    }
                    String scenarioName = ChatColor.stripColor(e.getView().getTopInventory().getTitle()).replace("Options for ", "").trim();
                    String itemStackName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                    Class<? extends Scenario> aClass = null;
                    for (Class<? extends Scenario> scenariosClass : Scenario.getRepertoriedScenariosClasses()) {
                        Method method = null;
                        try {
                            method = scenariosClass.getMethod("getName");
                        } catch (NoSuchMethodException noSuchMethodException) {
                            noSuchMethodException.printStackTrace();
                        }
                        if (method == null)
                            continue;
                        String obj = null;
                        try {
                            obj = (String) method.invoke(null);
                        } catch (IllegalAccessException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        } catch (InvocationTargetException invocationTargetException) {
                            invocationTargetException.printStackTrace();
                        }
                        if (obj == null)
                            continue;

                        if (obj.trim().equals(scenarioName))
                            aClass = scenariosClass;
                    }
                    Scenario scenario = null;
                    boolean value = false;
                    for (Scenario gameScenario : game.getScenarios()) {
                        if (gameScenario.getClass().equals(aClass)) {
                            value = true;
                            scenario = gameScenario;
                            break;
                        }
                    }
                    if (!value) {
                        try {
                            scenario = aClass.getConstructor(UHCGame.class).newInstance(game);
                        } catch (InstantiationException instantiationException) {
                            instantiationException.printStackTrace();
                        } catch (IllegalAccessException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        } catch (InvocationTargetException invocationTargetException) {
                            invocationTargetException.printStackTrace();
                        } catch (NoSuchMethodException noSuchMethodException) {
                            noSuchMethodException.printStackTrace();
                        }
                    }
                    for (ScenarioGetter annotation : scenario.getScenarioGetAnnotations()) {
                        try {
                            Class<?> aClass1 = null;
                            ArrayList<String> lore = new ArrayList<>();
                            try {
                                aClass1 = scenario.getScenarioGetMethod(annotation.name()).getReturnType();
                            } catch (NoSuchMethodException noSuchMethodException) {
                                noSuchMethodException.printStackTrace();
                            }
                            if (itemStackName.equals(annotation.name())) {
                                if (aClass1 == Boolean.class || aClass1 == boolean.class) {
                                    if (e.getClick().isLeftClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, true);
                                    if (e.getClick().isRightClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, false);
                                    if (e.getClick().isCreativeAction())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(UHCGame.class).newInstance(game)));
                                    lore.add(0, String.valueOf((boolean) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Integer.class || aClass1 == int.class) {
                                    int valueToUp = 0;
                                    if (e.getClick().isShiftClick())
                                        valueToUp = 10;
                                    else
                                        valueToUp = 1;

                                    if (e.getClick().isLeftClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, (int) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario) + valueToUp);
                                    if (e.getClick().isRightClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, (int) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario) - valueToUp);
                                    if (e.getClick().isCreativeAction())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(UHCGame.class).newInstance(game)));
                                    lore.add(0, String.valueOf((int) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Double.class || aClass1 == double.class) {
                                    float valueToUp = 0;
                                    if (e.getClick().isShiftClick())
                                        valueToUp = 1;
                                    else
                                        valueToUp = 0.1f;

                                    if (e.getClick().isLeftClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, new BigDecimal((double) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)).add(new BigDecimal(valueToUp)).setScale(1, RoundingMode.HALF_UP).doubleValue());
                                    if (e.getClick().isRightClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, new BigDecimal((double) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)).subtract(new BigDecimal(valueToUp)).setScale(1, RoundingMode.HALF_UP).doubleValue());
                                    if (e.getClick().isCreativeAction())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(UHCGame.class).newInstance(game)));
                                    lore.add(0, String.valueOf((double) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == Float.class || aClass1 == float.class) {
                                    float valueToUp = 0;
                                    if (e.getClick().isShiftClick())
                                        valueToUp = 1;
                                    else
                                        valueToUp = 0.1f;

                                    if (e.getClick().isLeftClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, new BigDecimal((float) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)).add(new BigDecimal(valueToUp)).setScale(1, RoundingMode.HALF_UP).floatValue());
                                    if (e.getClick().isRightClick())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, new BigDecimal((float) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)).subtract(new BigDecimal(valueToUp)).setScale(1, RoundingMode.HALF_UP).floatValue());
                                    if (e.getClick().isCreativeAction())
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(UHCGame.class).newInstance(game)));
                                    lore.add(0, String.valueOf((float) scenario.getScenarioGetMethod(annotation.name()).invoke(scenario)));
                                } else if (aClass1 == List.class) {
                                    //todo list
                                } else {
                                    //invalid field
                                }
                                ItemMeta meta = e.getCurrentItem().getItemMeta();
                                meta.setLore(lore);
                                e.getCurrentItem().setItemMeta(meta);
                            }
                        } catch (Exception err) {
                            Logger.getGeneralLogger().log(err);
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
}
