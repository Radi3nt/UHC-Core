package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void OnDamageEvent(EntityDamageEvent e) {
        if (e.getDamage() != 0) {
            if (e.getEntity() instanceof Player) {
                UHCGame game = UHCPlayer.thePlayer((Player) e.getEntity()).getGameData().getGame();
                if (e.getEntity().isDead() || ((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
                    Player player = (Player) e.getEntity();
                    UHCPlayer uhcPlayer = UHCPlayer.thePlayer((Player) e.getEntity());
                    if (uhcPlayer.isInGame()) {
                        e.setCancelled(true);
                        uhcPlayer.sendTitle(ChatColor.RED + "You died", ChatColor.GRAY + "You can only spectate the game", 5, 5 * 20, 5);

                        uhcPlayer.setChat(game.getData().getDeathChat());
                        player.setMaxHealth(20.0D);
                        player.setHealth(20.0D);


                        uhcPlayer.getPlayerStats().refresh();

                        //uhcPlayer.getGameData().setCanBeRespawned(true);
                        UHCPlayer lgDamager = null;
                        if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                            EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                            if (e1.getDamager() instanceof Player) {
                                if (!game.getPvP().isPvp()) {
                                    lgDamager = UHCPlayer.thePlayer((Player) e1.getDamager());

                                }
                            }
                            if (e1.getDamager() instanceof Projectile) {
                                Projectile projectile = (Projectile) e1.getDamager();
                                if (projectile.getShooter() instanceof Player) {
                                    if (!game.getPvP().isPvp()) {
                                        lgDamager = UHCPlayer.thePlayer((Player) projectile.getShooter());
                                    }
                                }
                            }
                        }

                        /*
                        if (!uhcPlayer.getGameData().isInCouple() || !uhcPlayer.getGameData().getCouple().getGameData().isDead()) {
                            for (UHCPlayer lgp2 : uhcPlayer.getGameData().getGame().getGamePlayers()) {
                                if (lgp2.getGameData().getRole().getRoleIdentity().equals(Sorciere.getStaticRoleIdentity()) && uhcPlayer.getGameData().getGame().getGameTimer().getDays() > uhcPlayer.getGameData().getGame().getParameters().getDayRoleDivulged()-1) {
                                    Sorciere role = (Sorciere) lgp2.getGameData().getRole();
                                    if (role.hasPower()) {
                                        lgp2.sendMessage(getPrefix() + " " + getPrefixPrivé() + " " + ChatColor.GOLD + player.getName() + " est mort, veut tu le réssusciter ? Tu a 5 secondes pour répondre.");
                                        TextComponent tc = new TextComponent();
                                        tc.setText(ChatColor.DARK_GREEN + "Réssusciter ce joueur ➤ " + uhcPlayer.getName());
                                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg role respawn " + uhcPlayer.getName()));
                                        //tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "Réssusciter cette personne").create()));

                                        lgp2.getPlayer().spigot().sendMessage(tc);
                                    }
                                }
                                    if (lgp2.getGameData().getRole().getRoleIdentity().equals(LGInfect.getStaticRoleIdentity()) && uhcPlayer.getGameData().getGame().getGameTimer().getDays() > uhcPlayer.getGameData().getGame().getParameters().getDayRoleDivulged()-1) {
                                        LGInfect role = (LGInfect) lgp2.getGameData().getRole();
                                        if (role.hasPower()) {
                                            if (lgDamager != null && lgDamager.isInGame() && (lgDamager.getGameData().getRole().getRoleIdentity().getRoleType() == RoleType.LOUP_GAROU || lgDamager.getGameData().getRole().isInfected())){
                                                lgp2.sendMessage(getPrefix() + " " + getPrefixPrivé() + " " + ChatColor.GOLD + player.getName() + " est mort, veut tu le réssusciter ? Tu a 5 secondes pour répondre.");
                                                TextComponent tc = new TextComponent();
                                                tc.setText(ChatColor.DARK_GREEN + "Réssusciter ce joueur ➤ " + uhcPlayer.getName());
                                                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg role respawn " + uhcPlayer.getName()));
                                                //tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "Réssusciter cette personne").create()));

                                                lgp2.getPlayer().spigot().sendMessage(tc);
                                            }
                                        }
                                    }
                            }
                        }

                         */

                        uhcPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);

                        Reason reason = Reason.KILLED_UNDEFINED;
                        if (lgDamager != null)
                            reason = Reason.KILLED_BY_PLAYER;
                        else if (e instanceof EntityDamageByEntityEvent)
                            reason = Reason.KILLED_BY_MOB;

                        Bukkit.getPluginManager().callEvent(new UHCPlayerKilledEvent(game, uhcPlayer, lgDamager, reason, player.getLocation()));


                        /*
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                npc.spectateFix(player, player.getPlayer().getLocation().getYaw(), true);
                                //todo sound ENTITY_ELDER_GUARDIAN_AMBIENT

                                if (uhcPlayer.getGameData().getGame() == null) {
                                    if (uhcPlayer.getPlayer() != null)
                                        npc.spectate(uhcPlayer.getPlayer(), false);
                                }

                                if (i < 5 * 20 && !uhcPlayer.getGameData().canBeRespawned() || (game.getGameTimer().getDays() < game.getParameters().getDayRoleDivulged() == game.getParameters().isRespawnBeforeRoleDivulged()) && uhcPlayer.getPlayer() != null) {
                                    cancel();
                                    npc.spectate(uhcPlayer.getPlayer(), false);
                                    npc.destroy();
                                    player.setGameMode(GameMode.SURVIVAL);
                                    uhcPlayer.getGameData().getGame().scatter(uhcPlayer);
                                    uhcPlayer.getGameData().setDead(false);
                                    uhcPlayer.setChat(GameChatI);
                                    game.updateKill(false);
                                    if (uhcPlayer.getGameData().getGame() != null) {
                                        if (game.getGameTimer().getDays() < game.getParameters().getDayRoleDivulged()) {
                                            LoupGarouUHC.broadcastMessage(getPrefix() + " " + ChatColor.DARK_RED + uhcPlayer.getPlayer().getName() + ChatColor.RED + " has been respawned");
                                            int items = game.getParameters().getNumberOfBlockRemovedWhenRespawn();
                                            for (ItemStack itemStack : uhcPlayer.getPlayer().getInventory().getContents()) {
                                                if (itemStack != null && (itemStack.getType().isBlock() || !uhcPlayer.getGameData().getGame().getParameters().isOnlyBlockCanBeRemoved())) {
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
                                                                rare = true;
                                                        }

                                                        if (rareItems.contains(itemStack.getType()))
                                                            rare = true;

                                                        if (rare)
                                                            continue;
                                                    }
                                                    if (random.nextInt(100) < game.getParameters().getPercentageOfChanceToRemove()) {
                                                        for (int i = 0; i < random.nextInt(game.getParameters().getNumberOfBlockRemovedWhenRespawn()); i++) {
                                                            try {
                                                                itemStack.setAmount(itemStack.getAmount() - 1);
                                                                items--;
                                                            } catch (Exception e) {

                                                            }
                                                            if (items <= 0) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }

                                if (i == 5 * 20 || uhcPlayer.getGameData().isInCouple() && uhcPlayer.getGameData().getCouple().getGameData().isDead()) {
                                    if (game.isRoleTrolled() && game.getGameTimer().getTicks() < game.getParameters().getTrollEndTime()) {
                                        SecureRandom random = new SecureRandom();
                                        uhcPlayer.getGameData().setRole(game.getRoles().get(random.nextInt(game.getRoles().size())));
                                    }
                                    uhcPlayer.getGameData().setCanVote(false);
                                    if (player != null) {
                                        npc.spectate(uhcPlayer.getPlayer(), false);
                                        (player.getLocation().getWorld().spawn(playerloc.clone().add(0.5, 0.5, 0.5), ExperienceOrb.class)).setExperience(player.getTotalExperience() / 17);
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
                                            if (item != null) {
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
                                    }
                                    if (uhcPlayer.getGameData().isInCouple() && uhcPlayer.getGameData().getCouple().getGameData().isDead()) {
                                        game.kill(uhcPlayer, Reason.LOVE, playerloc);
                                    } else {
                                        game.kill(uhcPlayer, Reason.TUÉ, playerloc);
                                    }
                                    if (game.getGameTimer() != null) {
                                        if (finalLgDamager != null) {
                                            Bukkit.getPluginManager().callEvent(new UHCPlayerKilledEvent(game, uhcPlayer, finalLgDamager, playerloc));
                                            uhcPlayer.getGameData().setKiller(finalLgDamager);
                                        }
                                    }


                                    uhcPlayer.getGameData().setCanBeRespawned(false);
                                    if (uhcPlayer.getGameData().isInCouple() && uhcPlayer.getGameData().isDead() && !uhcPlayer.getGameData().getCouple().getGameData().isDead()) {
                                        uhcPlayer.getGameData().getCouple().getPlayer().damage(20);
                                        //uhcPlayer.KilledgetCouple().setCouple(null);
                                        uhcPlayer.getGameData().setCouple(null);
                                    }
                                    if (uhcPlayer.getGameData().isInCouple() && !uhcPlayer.getGameData().isDead() && uhcPlayer.getGameData().getCouple().getGameData().isDead()) {
                                        uhcPlayer.getPlayer().damage(20);
                                        //uhcPlayer.getCouple().setCouple(null);
                                        uhcPlayer.getGameData().setCouple(null);
                                    }
                                    uhcPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                    uhcPlayer.getPlayer().setFlying(true);
                                }
                                if (i == 15 * 20) {
                                    cancel();
                                    npc.destroy();

                                }
                                i++;
                            }
                        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1L, 0L);
                         */
                    }
                }
            }
        }
    }

}
