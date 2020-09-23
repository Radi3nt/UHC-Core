package fr.radi3nt.loupgarouuhc.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.Reason;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.npc.NPC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.events.OnKill;
import fr.radi3nt.loupgarouuhc.events.OnKilled;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;
import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class DamageEvent implements Listener {

    @EventHandler
    public void OnDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            LGGame game = LGPlayer.thePlayer((Player) e.getEntity()).getGame();
            if (game!=null) {
                if (game.getGameTimer() != null) {
                    if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                        EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                        if (e1.getDamager() instanceof Player) {
                            if (!game.getGameTimer().isPvp()) {
                                e.setCancelled(true);
                            }
                        }
                        if (e1.getDamager() instanceof Projectile) {
                            Projectile projectile = (Projectile) e1.getDamager();
                            if (projectile.getShooter() instanceof Player) {
                                if (!game.getGameTimer().isPvp()) {
                                    e.setCancelled(true);
                                }
                            } else if (!game.getGameTimer().isDegas()) {
                                e.setCancelled(true);
                            }
                        } else if (!game.getGameTimer().isDegas()) {
                            e.setCancelled(true);
                        }
                    } else if (!game.getGameTimer().isDegas()) {
                        e.setCancelled(true);
                    }
                } else if (!game.getGameTimer().isDegas()) {
                    e.setCancelled(true);
                }
            }
            if (e.getEntity().isDead() || ((Player) e.getEntity()).getHealth()-e.getDamage()<=0) {
                Player player = (Player) e.getEntity();
                LGPlayer lgp = LGPlayer.thePlayer((Player) e.getEntity());
                if (lgp.getGame()!=null && lgp.getRole()!=null) {

                    e.setCancelled(true);
                    player.setMaxHealth(20.0D);
                    player.setHealth(20.0D);
                    lgp.sendTitle(ChatColor.RED + "You died", ChatColor.GRAY + "You can only spectate the game", 5, 5*20, 5);

                    lgp.setCanBeRespawned(true);
                    for (LGPlayer lgp2 : lgp.getGame().getGamePlayers()) {
                        if (lgp2.getRole().getRoleSort() == RoleSort.SORCIERE && lgp.getGame().getGameTimer().getDays()>1) {

                            lgp2.sendMessage(prefix + " " + prefixPrivé + " " + ChatColor.GOLD + player.getName() + " est mort, veut tu le réssusciter ? Tu a 5 secondes pour répondre.");
                            TextComponent tc = new TextComponent();
                            tc.setText(ChatColor.DARK_GREEN + "Réssusciter ce joueur ➤ " + lgp.getName());
                            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg role respawn " + lgp.getName()));
                            //tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "Réssusciter cette personne").create()));

                            lgp2.getPlayer().spigot().sendMessage(tc);
                        }
                        if (lgp.getGame().getGameTimer()!=null && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                            if (lgp2.getRole().getRoleSort() == RoleSort.LG_INFECTE && lgp.getGame().getGameTimer().getDays()>1) {
                                lgp2.sendMessage(prefix + " " + prefixPrivé + " " + ChatColor.GOLD + player.getName() + " est mort, veut tu le réssusciter ? Tu a 5 secondes pour répondre.");
                                TextComponent tc = new TextComponent();
                                tc.setText(ChatColor.DARK_GREEN + "Réssusciter ce joueur ➤ " + lgp.getName());
                                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg role respawn " + lgp.getName()));
                                //tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "Réssusciter cette personne").create()));

                                lgp2.getPlayer().spigot().sendMessage(tc);
                            }
                        }
                    }
                    /*
                    ReflectCorpse rp = null;
                    try {
                        rp = new ReflectCorpse(lgp.getName(), lgp.getPlayer().getLocation());
                        rp.spawn();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    //rp.destroy(); for delete the corpse;

                     */
                    NPC npc = new NPC(lgp.getName(), lgp.getPlayer().getLocation(), plugin);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        npc.addRecipient(onlinePlayer);
                    }
                    EntityPlayer playerNMS = ((CraftPlayer) lgp.getPlayer()).getHandle();
                    if (playerNMS.getProfile() != null) {
                        try {
                            GameProfile profile = playerNMS.getProfile();
                            Property property = profile.getProperties().get("textures").iterator().next();
                            String texture = property.getValue();
                            String signature = property.getSignature();
                            npc.setSkin(texture, signature);
                        } catch (Exception e1) {

                        }
                    }
                    npc.spawn(false, true);
                    npc.setSleep(true, npc.getDirectionInversed(lgp.getPlayer().getLocation()), false);


                    Location playerloc = lgp.getPlayer().getLocation();

                    lgp.getPlayer().setGameMode(GameMode.SPECTATOR);
                    npc.spectateFix(lgp.getPlayer(), lgp.getPlayer().getLocation().getYaw(), true);


                    if (lgp.getGame().getGameTimer()!=null && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                        EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                        if (e1.getDamager() instanceof Player) {
                            Bukkit.getPluginManager().callEvent(new OnKill(lgp.getGame(), LGPlayer.thePlayer((Player) e1.getDamager()), lgp));
                        }
                    }
                    lgp.setChat(DeadChatI);


                    new BukkitRunnable(){
                        int i = 0;

                        @Override
                        public void run() {
                            if (lgp.getGame() == null) {
                                cancel();
                                npc.spectate(lgp.getPlayer(), false);
                                npc.destroy();
                            }


                            if (i < 5 * 20 && !lgp.canBeRespawned() || (game.getGameTimer().getDays() < game.getParameters().getDayRoleDivulged() == game.getParameters().isRespawnBeforeRoleDivulged())) {
                                cancel();
                                npc.spectate(lgp.getPlayer(), false);
                                player.setGameMode(GameMode.SURVIVAL);
                                lgp.getGame().scatter(lgp);
                                lgp.setGame(game);
                                lgp.setDead(false);
                                lgp.setChat(GameChatI);
                                game.updateKill(false);
                                if (game.getGameTimer().getDays()<game.getParameters().getDayRoleDivulged()) {
                                    Bukkit.broadcastMessage(prefix + " " + ChatColor.DARK_RED + lgp.getPlayer().getName() + ChatColor.RED + " has been respawned");
                                    int items = game.getParameters().getNumberOfBlockRemovedWhenRespawn();
                                    for (ItemStack itemStack : lgp.getPlayer().getInventory().getContents()) {
                                        if (itemStack != null && (itemStack.getType().isBlock() || !lgp.getGame().getParameters().isOnlyBlockCanBeRemoved())) {
                                            SecureRandom random = new SecureRandom();
                                            if (game.getParameters().isPreserveRareItem()) {
                                                boolean rare = false;
                                                ArrayList<Material> rareItems = new ArrayList<>();
                                                rareItems.add(Material.DIAMOND_SWORD);
                                                rareItems.add(Material.DIAMOND);
                                                rareItems.add(Material.ANVIL);
                                                rareItems.add(Material.OBSIDIAN);
                                                for (Enchantment enchantment : Enchantment.values()) {
                                                    if (itemStack.containsEnchantment(enchantment))
                                                        rare=true;
                                                }

                                                if (rareItems.contains(itemStack.getType()))
                                                    rare=true;

                                                if (rare)
                                                    continue;
                                            }
                                            if (random.nextInt(100)<game.getParameters().getPercentageOfChanceToRemove()) {
                                                for (int i = 0; i < random.nextInt(game.getParameters().getNumberOfBlockRemovedWhenRespawn()); i++) {
                                                    try {
                                                        itemStack.setAmount(itemStack.getAmount() - 1);
                                                        items--;
                                                    } catch (Exception e) {

                                                    }
                                                    if (items<=0) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }

                            if (i==5*20) {
                                if (lgp.getCouple() == null || lgp.getCouple().getCouple() != null) {
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "========== ♦ =========");
                                    Bukkit.broadcastMessage(ChatColor.GREEN + "Le village a perdu un de ses membres:");
                                    Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + lgp.getName() + ChatColor.GREEN + " est mort, il était " + ChatColor.ITALIC + lgp.getRole().getName());
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "=====================");
                                } else {
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "========== ♦ =========");
                                    Bukkit.broadcastMessage(ChatColor.GREEN + "Le village a perdu un de ses membres:");
                                    Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + lgp.getName() + ChatColor.GREEN + " qui c'est suicidé, il était " + ChatColor.ITALIC + lgp.getRole().getName());
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "=====================");
                                }
                                lgp.setCanVote(false);
                                lgp.setDiamondMined(0);
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item != null) {
                                        player.getWorld().dropItem(playerloc, item.clone());
                                        item.setAmount(0);
                                    }
                                }
                                for (ItemStack item : player.getInventory().getArmorContents()) {
                                    if (item != null) {
                                        player.getWorld().dropItem(playerloc, item.clone());
                                        item.setAmount(0);
                                    }
                                }
                                for (ItemStack item : player.getInventory().getExtraContents()) {
                                    if (item!=null) {
                                        player.getWorld().dropItem(playerloc, item.clone());
                                        item.setAmount(0);
                                    }
                                }
                                for (ItemStack item : player.getInventory().getStorageContents()) {
                                    if (item != null) {
                                        player.getWorld().dropItem(playerloc, item.clone());
                                        item.setAmount(0);
                                    }
                                }

                                if (game.getGameTimer() != null) {
                                    if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                                        EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                                        if (e1.getDamager() instanceof Player) {
                                            if (!game.getGameTimer().isPvp()) {
                                                Bukkit.getPluginManager().callEvent(new OnKilled(lgp.getGame(), lgp, LGPlayer.thePlayer((Player) e1.getDamager()), playerloc));
                                                lgp.setKiller(LGPlayer.thePlayer((Player) e1.getDamager()));

                                            }
                                        }
                                        if (e1.getDamager() instanceof Projectile) {
                                            Projectile projectile = (Projectile) e1.getDamager();
                                            if (projectile.getShooter() instanceof Player) {
                                                if (!game.getGameTimer().isPvp()) {
                                                    Bukkit.getPluginManager().callEvent(new OnKilled(lgp.getGame(), lgp, LGPlayer.thePlayer((Player) projectile.getShooter()), playerloc));
                                                    lgp.setKiller(LGPlayer.thePlayer((Player) projectile.getShooter()));

                                                }
                                            }
                                        }
                                    }
                                }

                                lgp.getGame().kill(lgp, Reason.TUÉ, false, playerloc);
                                lgp.setCanBeRespawned(false);
                                if (lgp.getCouple() != null && lgp.isDead() && !lgp.getCouple().isDead()) {
                                    lgp.getCouple().getPlayer().damage(20);
                                    //lgp.getCouple().setCouple(null);
                                    lgp.setCouple(null);
                                }
                                if (lgp.getCouple()!=null && !lgp.isDead() && lgp.getCouple().isDead()) {
                                    lgp.getPlayer().damage(20);
                                    //lgp.getCouple().setCouple(null);
                                    lgp.setCouple(null);
                                }
                                lgp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                npc.spectate(lgp.getPlayer(), false);
                            }
                            if (i==15*20) {
                                cancel();
                                npc.destroy();
                            }
                            i++;
                        }
                    }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1L, 0L);
                }
            }
        }
    }

}
