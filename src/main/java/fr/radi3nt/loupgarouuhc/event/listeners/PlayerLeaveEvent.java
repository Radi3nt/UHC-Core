package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.loupgarouuhc.classes.game.Reason;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Hologram;
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
        lgp.getPlayerStats().refresh();
        Location playerloc = p.getLocation();
        PlayerInventory inventory = p.getInventory();
        lgp.saveStats();
        lgp.saveLang();
        e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getName());
        if (lgp.isInGame() && lgp.getGameData().hasRole() && !lgp.getGameData().isDead()) {
            new BukkitRunnable() {
                final LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer().getUniqueId());
                int i = 0;

                @Override
                public void run() {
                    if (!lgp.isInGame()) {
                        cancel();
                    }
                    if (lgp.isInGame() && (i >= lgp.getGameData().getGame().getParameters().getDisconnectTimeout() || (lgp.getGameData().getGame().getPvP().isPvp() && !lgp.getGameData().getGame().getParameters().isCanReconnectInPvp()))) {
                        lgp.getGameData().getGame().kill(lgp, Reason.DISCONNECTED, false, playerloc);
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

                        lgp.getStats().setKills(lgp.getStats().getKills() + lgp.getGameData().getKills());
                        lgp.getGameData().setKills(0);


                        lgp.getGameData().setCanVote(false);
                        lgp.getGameData().setKiller(null);
                        lgp.getGameData().setCanBeRespawned(false);
                        lgp.getGameData().setCanVote(false);
                        lgp.getGameData().setCouple(null);

                        getPlayers().remove(lgp);
                        lgp.remove();
                        this.cancel();
                    }
                    if (i > 20 && lgp.getPlayer() != null && lgp.getPlayer().isOnline()) {
                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimer(getPlugin(), 1L, 1L);
        }
        if (!lgp.isInGame() || lgp.getGameData().isDead()) {
            getGameInstance().getGamePlayers().remove(lgp);
            getPlayers().remove(lgp);
            lgp.remove();
        }
    }
}
