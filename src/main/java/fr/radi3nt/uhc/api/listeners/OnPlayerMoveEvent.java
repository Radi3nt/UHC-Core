/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.api.stats.Hologram;
import fr.radi3nt.uhc.api.utilis.Maths;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {


    @EventHandler
    public void OnPlayerMoveEvent(PlayerMoveEvent e) {
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            Location location = holoStats.getLocation();
            int viewDistance = 64;
            if (location.getWorld().equals(e.getFrom().getWorld())) {
                if (location.getWorld().equals(e.getTo().getWorld())) {
                    showAll(holoStats, e.getPlayer());
                }
                if (Maths.distanceIn2D(location, e.getTo()) < (float) viewDistance) {
                    showAll(holoStats, e.getPlayer());
                } else {
                    hideAll(holoStats, e.getPlayer());
                }
            } else {
                hideAll(holoStats, e.getPlayer());
            }
        }
    }

    private void hideAll(HoloStats holoStats, Player player) {
        for (Hologram hologram : holoStats.getHologramsStand()) {
            hologram.hide(player);
        }
    }

    private void showAll(HoloStats holoStats, Player player) {
        for (Hologram hologram : holoStats.getHologramsStand()) {
            hologram.display(player);
        }
    }
}
