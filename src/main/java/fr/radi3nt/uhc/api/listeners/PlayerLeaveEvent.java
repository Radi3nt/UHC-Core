package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.reasons.Reason;
import fr.radi3nt.uhc.api.game.reasons.ReasonDisconnected;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.api.stats.Hologram;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;


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
        if (lgp.isPlaying()) {
            new BukkitRunnable() {
                final UUID uuid = e.getPlayer().getUniqueId();
                int i = 0;

                @Override
                public void run() {
                    UHCPlayer lgp = UHCPlayer.thePlayer(uuid);
                    if (!lgp.isPlaying()) {
                        lgp.clearWaitingGame();
                        UHCCore.getPlayers().remove(lgp);
                        lgp.remove();
                        cancel();
                    }
                    if (lgp.isPlaying() && (i >= lgp.getGameData().getGame().getParameters().getDisconnectParameters().getDisconnectTimeout() * 60 * 20 || (lgp.getGameData().getGame().getPvP().isPvp() && !lgp.getGameData().getGame().getParameters().getDisconnectParameters().canReconnectInPvp()))) {
                        Bukkit.getPluginManager().callEvent(new UHCPlayerKilledEvent(lgp.getGameData().getGame(), lgp, new ReasonDisconnected()));
                        lgp.clearWaitingGame();
                        UHCCore.getPlayers().remove(lgp);
                        lgp.remove();
                        this.cancel();
                    }
                    if (i > 20 && lgp.isOnline()) {
                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimer(UHCCore.getPlugin(), 1L, 1L);
        }
        if (!lgp.isPlaying()) {
            lgp.clearWaitingGame();
            UHCCore.getPlayers().remove(lgp);
            lgp.remove();
        }
    }
}
