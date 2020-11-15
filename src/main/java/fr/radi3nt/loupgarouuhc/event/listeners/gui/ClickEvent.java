package fr.radi3nt.loupgarouuhc.event.listeners.gui;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.GUIs.*;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.gui.guis.MainGUI;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioCommands;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
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

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class ClickEvent implements Listener {

    @EventHandler
    public void OnClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            if (MainGUI.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(MainGUI.createStartItem())) {
                    getGameInstance().updateStart();
                }
                if (e.getCurrentItem().isSimilar(MainGUI.createOptionItem())) {
                    player.openInventory(OptionsItems.createGUI(player));
                }
                if (e.getCurrentItem().isSimilar(MainGUI.createManageItem())) {
                    player.openInventory(ManageGui.createGUI(player));
                }
            }
            if (ManageGui.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(ManageGui.createGameItem())) {
                    player.openInventory(ManageGameGui.createGUI(player));
                }
                if (e.getCurrentItem().isSimilar(ManageGui.createPlayerItem())) {

                }
                if (e.getCurrentItem().isSimilar(ManageGui.createBackItem())) {
                    player.openInventory(ManageGui.createGUI(player));
                }
            }
            if (OptionsItems.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(OptionsItems.createRolesItem())) {
                    player.openInventory(RoleConfigGui.createGUI(player, 0, RoleConfigGui.PageType.VILAGER));
                }
                if (e.getCurrentItem().isSimilar(OptionsItems.createBackItem())) {
                    player.openInventory(MainGUI.createGUI(player));
                    return;
                }
            }
            if (ManageGameGui.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(ManageGameGui.createSkipItem())) {
                    if (UHCPlayer.thePlayer(player).isInGame()) {
                        UHCPlayer.thePlayer(player).getGameData().getGame().getGameTimer().setDay(UHCPlayer.thePlayer(player).getGameData().getGame().getGameTimer().getDays() + 1);
                    }
                }
                if (e.getCurrentItem().isSimilar(ManageGameGui.createBackItem())) {
                    player.openInventory(ManageGui.createGUI(player));
                }
            }
            if (RoleConfigGui.checkInventoryView(e.getView())) {
                e.setCancelled(true);

                if (e.getCurrentItem().isSimilar(RoleConfigGui.createBackItem())) {
                    player.openInventory(OptionsItems.createGUI(player));
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createNextPageItem())) {
                    try {
                        if (RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()), RoleConfigGui.getPageType(e.getView())) != null) {
                            player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()), RoleConfigGui.getPageType(e.getView())));
                        }
                    } catch (Exception e1) {

                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createBackPageItem())) {
                    try {
                        if (RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 2, RoleConfigGui.getPageType(e.getView())) != null) {
                            player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 2, RoleConfigGui.getPageType(e.getView())));
                        }
                    } catch (Exception e1) {

                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createRoleItem(RoleConfigGui.PageType.SOLO.getName(), RoleConfigGui.PageType.SOLO.getMaterial(), false))) {
                    try {
                        player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 1, RoleConfigGui.PageType.SOLO));
                    } catch (Exception e1) {

                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createRoleItem(RoleConfigGui.PageType.VILAGER.getName(), RoleConfigGui.PageType.VILAGER.getMaterial(), false))) {
                    try {
                        player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 1, RoleConfigGui.PageType.VILAGER));
                    } catch (Exception e1) {

                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createRoleItem(RoleConfigGui.PageType.LOUPGAROU.getName(), RoleConfigGui.PageType.LOUPGAROU.getMaterial(), false))) {
                    try {
                        player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 1, RoleConfigGui.PageType.LOUPGAROU));
                    } catch (Exception e1) {

                    }
                    return;
                }
                for (RoleIdentity sort : Role.getRoleLinkByStringKey().keySet()) {
                    if (e.getCurrentItem().getItemMeta() != null && sort.getName(UHCPlayer.thePlayer(player).getLanguage()).equals(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                        String key = sort.getId();
                        if (e.getClick().isLeftClick()) {
                            if (e.getClick().isShiftClick()) {
                                if (LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) + 5 <= 32) {
                                    LoupGarouUHC.getRoleNumber().put(key, LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) + 5);
                                } else {
                                    LoupGarouUHC.getRoleNumber().put(key, 32);
                                }
                            } else {
                                if (LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) + 5 <= 32) {
                                    LoupGarouUHC.getRoleNumber().put(key, LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) + 1);
                                }
                            }
                        }
                        if (e.getClick().isRightClick()) {
                            if (e.getClick().isShiftClick()) {
                                if (LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) - 5 >= 0) {
                                    LoupGarouUHC.getRoleNumber().put(key, LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) - 5);
                                } else {
                                    LoupGarouUHC.getRoleNumber().put(key, 0);
                                }
                            } else {
                                if (LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) - 1 >= 0) {
                                    LoupGarouUHC.getRoleNumber().put(key, LoupGarouUHC.getRoleNumber().getOrDefault(key, 0) - 1);
                                }
                            }
                        }
                    }
                }
                try {
                    for (RoleIdentity role : Role.getRoleLinkByStringKey().keySet()) {
                        if (getRoleNumber().containsKey(role.getId())) {
                            getRolesConfig().set("Roles." + role.getId(), getRoleNumber().getOrDefault(role.getId(), 0));
                        }
                    }

                } catch (Exception err) {
                    Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
                    Logger.getGeneralLogger().log(err);
                }
                LoupGarouUHC.saveRoleFile();
                RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 1, e.getInventory(), RoleConfigGui.getPageType(e.getView()));
            }
            if (e.getView().getTopInventory().getTitle().contains(ChatColor.GOLD + "Scenarios ")) {
                LGGame game = UHCPlayer.thePlayer(player).getGameData().getGame();
                if (game == null || game.getGameState() != GameState.LOBBY) {
                    game = getGameInstance();
                }
                String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                int currentPage = Integer.parseInt(ChatColor.stripColor(e.getView().getTitle()).replace("Scenarios ", "").trim()) - 1;
                Class<?> aClass = null;
                for (Class<? extends Scenario> scenariosClass : Scenario.getScenariosClasses()) {
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

                    if (obj.equals(itemName))
                        aClass = scenariosClass;
                }
                if (aClass != null) {
                    Scenario scenario = null;
                    try {
                        scenario = (Scenario) aClass.getConstructor(LGGame.class).newInstance(game);
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
                            game.getScenarios().add(scenario);
                            if (game.getGameState()==GameState.PLAYING) {
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
                        e.getWhoClicked().openInventory(ScenarioCommands.createInventory(e.getWhoClicked(), game.getScenarios())[currentPage]);
                    }
                }
                e.setCancelled(true);
            } else if (e.getView().getTopInventory().getTitle().contains(ChatColor.GOLD + "Options for ") && e.getCurrentItem() != null) {
                if (e.getCurrentItem() != null) {
                    LGGame game = UHCPlayer.thePlayer(player).getGameData().getGame();
                    if (game == null || game.getGameState()!=GameState.LOBBY) {
                        game = getGameInstance();
                    }
                    String scenarioName = ChatColor.stripColor(e.getView().getTopInventory().getTitle()).replace("Options for ", "").trim();
                    String itemStackName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                    Class<? extends Scenario> aClass = null;
                    for (Class<? extends Scenario> scenariosClass : Scenario.getScenariosClasses()) {
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
                            scenario = aClass.getConstructor(LGGame.class).newInstance(game);
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
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(LGGame.class).newInstance(game)));
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
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(LGGame.class).newInstance(game)));
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
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(LGGame.class).newInstance(game)));
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
                                        scenario.getScenarioSetMethod(annotation.name()).invoke(scenario, scenario.getScenarioGetMethod(annotation.name()).invoke(scenario.getClass().getConstructor(LGGame.class).newInstance(game)));
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
