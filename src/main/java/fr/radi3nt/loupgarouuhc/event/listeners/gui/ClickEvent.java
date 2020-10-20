package fr.radi3nt.loupgarouuhc.event.listeners.gui;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.GUIs.*;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;
import static org.bukkit.Bukkit.broadcastMessage;

public class ClickEvent implements Listener {

    @EventHandler
    public void OnClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            if (MainGUI.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(MainGUI.createStartItem())) {
                    GameInstance.updateStart();
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
                    player.openInventory(RoleConfigGui.createGUI(player, 0));
                }
                if (e.getCurrentItem().isSimilar(OptionsItems.createBackItem())) {
                    player.openInventory(MainGUI.createGUI(player));
                    return;
                }
            }
            if (ManageGameGui.checkInventoryView(e.getView())) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(ManageGameGui.createSkipItem())) {
                    if (LGPlayer.thePlayer(player).getGameData().getGame() != null) {
                        LGPlayer.thePlayer(player).getGameData().getGame().getGameTimer().setDay(LGPlayer.thePlayer(player).getGameData().getGame().getGameTimer().getDays() + 1);
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
                        if (RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView())) != null) {
                            player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView())));
                        }
                    } catch (Exception e1) {

                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(RoleConfigGui.createBackPageItem())) {
                    try {
                        if (RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 2) != null) {
                            player.openInventory(RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView()) - 2));
                        }
                    } catch (Exception e1) {

                    }
                    return;
                }
                for (RoleSort sort : RoleSort.values()) {
                    if (e.getCurrentItem().getItemMeta() != null && sort.getName(LGPlayer.thePlayer(player).getLanguage()).equals(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                        String key = "";
                        try {
                            for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet()) {
                                if (role.getValue().newInstance(new LGGame(parameters)).getRoleSort() == sort) {
                                    key = role.getKey();
                                }
                            }

                        } catch (Exception err) {
                            broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
                            err.printStackTrace();
                        }
                        if (rolesLink.containsKey(key)) {
                            if (e.getClick().isLeftClick()) {
                                if (e.getClick().isShiftClick()) {
                                    if (LoupGarouUHC.roleNumber.getOrDefault(key, 0) + 5 <= 32) {
                                        LoupGarouUHC.roleNumber.put(key, LoupGarouUHC.roleNumber.getOrDefault(key, 0) + 5);
                                    } else {
                                        LoupGarouUHC.roleNumber.put(key, 32);
                                    }
                                } else {
                                    if (LoupGarouUHC.roleNumber.getOrDefault(key, 0) + 5 <= 32) {
                                        LoupGarouUHC.roleNumber.put(key, LoupGarouUHC.roleNumber.getOrDefault(key, 0) + 1);
                                    }
                                }
                            }
                            if (e.getClick().isRightClick()) {
                                if (e.getClick().isShiftClick()) {
                                    if (LoupGarouUHC.roleNumber.getOrDefault(key, 0) - 5 >= 0) {
                                        LoupGarouUHC.roleNumber.put(key, LoupGarouUHC.roleNumber.getOrDefault(key, 0) - 5);
                                    } else {
                                        LoupGarouUHC.roleNumber.put(key, 0);
                                    }
                                } else {
                                    if (LoupGarouUHC.roleNumber.getOrDefault(key, 0) - 1 >= 0) {
                                        LoupGarouUHC.roleNumber.put(key, LoupGarouUHC.roleNumber.getOrDefault(key, 0) - 1);
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet()) {
                        if (roleNumber.containsKey(role.getKey())) {
                            RolesConfig.set("Roles." + role.getKey(), roleNumber.getOrDefault(role.getKey(), 0));
                        }
                    }

                } catch (Exception err) {
                    broadcastMessage("§4§lUne erreur est survenue lors de la sauvegarde des roles... Regardez la console !");
                    err.printStackTrace();
                }
                try {
                    RolesConfig.save(RoleConfigFile);
                } catch (IOException error) {
                    error.printStackTrace();
                }
                RoleConfigGui.createGUI(player, RoleConfigGui.getPage(e.getView())-1, e.getInventory());
            }
        }
    }

}
