package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.api.stats.Hologram;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;


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
        UHCPlayer lgp = UHCPlayer.thePlayer(e.getPlayer());
        lgp.getPlayerStats().refresh();
        Location playerloc = p.getLocation();
        PlayerInventory inventory = p.getInventory();
        lgp.saveStats();
        lgp.saveLang();
        e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getName());
        if (lgp.isInGame() && !lgp.getGameData().isDead()) {
            new BukkitRunnable() {
                final UHCPlayer lgp = UHCPlayer.thePlayer(e.getPlayer().getUniqueId());
                int i = 0;

                @Override
                public void run() {
                    if (!lgp.isInGame()) {
                        cancel();
                    }
                    if (lgp.isInGame() && (i >= lgp.getGameData().getGame().getParameters().getDisconnectParameters().getDisconnectTimeout() * 60 * 20 || (lgp.getGameData().getGame().getPvP().isPvp() && !lgp.getGameData().getGame().getParameters().getDisconnectParameters().canReconnectInPvp()))) {
                        lgp.getGameData().getGame().kill(lgp, Reason.DISCONNECTED, playerloc);
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


                        lgp.getGameData().setKiller(null);

                        UHCCore.getPlayers().remove(lgp);
                        lgp.remove();
                        this.cancel();
                    }
                    if (i > 20 && lgp.getPlayer() != null && lgp.getPlayer().isOnline()) {
                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimer(UHCCore.getPlugin(), 1L, 1L);
        }
        if (!lgp.isInGame() || lgp.getGameData().isDead()) {
            UHCCore.getPlayers().remove(lgp);
            lgp.remove();
        }
    }
}
