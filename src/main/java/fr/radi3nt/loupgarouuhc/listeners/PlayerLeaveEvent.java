package fr.radi3nt.loupgarouuhc.listeners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        //PACKETS THINGS
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            for (Hologram hologram : holoStats.getHologramsStand()) {
                hologram.hide(e.getPlayer());
            }
        }

        Player p = e.getPlayer();
        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        Location playerloc = p.getLocation();
        PlayerInventory inventory = p.getInventory();
        lgp.saveStats();
        e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getName());
        if (lgp.getGame() != null && lgp.getRole() != null && !lgp.isDead()) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    if (i>=lgp.getGame().getParameters().getDisconnectTimeout() || lgp.getGame().getGameTimer().getTicks() >= lgp.getGame().getParameters().getPvpActivate()) {
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "========== ♦ =========");
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Le village a perdu un de ses membres:");
                        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + lgp.getName() + ChatColor.GREEN + " est mort, il était " + ChatColor.ITALIC + lgp.getRole().getName());
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "=====================");
                        for (ItemStack item : inventory.getContents()) {
                            if (item != null) {
                                playerloc.getWorld().dropItem(playerloc, item.clone());
                                item.setAmount(0);
                            }
                        }
                        for (ItemStack item : inventory.getArmorContents()) {
                            if (item != null) {
                                playerloc.getWorld().dropItem(playerloc, item.clone());
                                item.setAmount(0);
                            }
                        }
                        for (ItemStack item : inventory.getExtraContents()) {
                            if (item != null) {
                                playerloc.getWorld().dropItem(playerloc, item.clone());
                                item.setAmount(0);
                            }
                        }
                        for (ItemStack item : inventory.getStorageContents()) {
                            if (item != null) {
                                playerloc.getWorld().dropItem(playerloc, item.clone());
                                item.setAmount(0);
                            }
                        }


                        lgp.getGame().getRoles().remove(lgp.getRole());
                        lgp.getGame().getGamePlayers().remove(lgp);
                        lgp.setChat(DeadChatI);
                        //Lightning effect
                        playerloc.getWorld().strikeLightningEffect(playerloc);
                        lgp.setDead(true);
                        if (lgp.getCouple()!=null) {
                            lgp.getCouple().getPlayer().damage(20);
                            //lgp.getCouple().setCouple(null);
                            lgp.setCouple(null);
                        }
                        lgp.getGame().getGamePlayers().remove(lgp);
                        lgp.getGame().updateKill(false);

                        lgp.setGame(null);

                        lgp.getStats().setKills(lgp.getStats().getKills() + lgp.getKills());
                        lgp.setKills(0);



                        lgp.setCanVote(false);
                        lgp.setDiamondMined(0);
                        lgp.setKiller(null);
                        lgp.setCanBeRespawned(false);
                        lgp.setCanVote(false);
                        lgp.setCouple(null);

                        players.remove(lgp);
                        lgp.remove();
                        this.cancel();
                    }
                    if (lgp.getPlayer()!=null) {
                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimer(plugin, 1L, 1L);
        }
        if (lgp.getGame()==null || lgp.isDead()) {
            lgp.setCanVote(false);
            lgp.setDiamondMined(0);
            lgp.setKiller(null);
            lgp.setCanBeRespawned(false);
            lgp.setCanVote(false);
            lgp.setCouple(null);
            GameInstance.getGamePlayers().remove(lgp);
            players.remove(lgp);
            lgp.remove();
        }
    }
}
