/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Hologram;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {


    @EventHandler
    public void OnPlayerMoveEvent(PlayerMoveEvent e) {
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            Location location = holoStats.getLocation();
            int viewDistance = 64;
            if (getDistanceBetween2Points(location, e.getTo()) != null) {
                if (getDistanceBetween2Points(location, e.getTo()) == null) {
                    for (Hologram hologram : holoStats.getHologramsStand()) {
                        if (!hologram.getViewers().contains(e.getPlayer())) {
                            hologram.display(e.getPlayer());
                        }
                    }
                }
                if (getDistanceBetween2Points(location, e.getTo()) < (float) viewDistance) {
                    for (Hologram hologram : holoStats.getHologramsStand()) {
                        if (!hologram.getViewers().contains(e.getPlayer())) {
                            hologram.display(e.getPlayer());
                        }
                    }
                } else {
                    for (Hologram hologram : holoStats.getHologramsStand()) {
                        hologram.hide(e.getPlayer());
                    }
                }
            } else {
                for (Hologram hologram : holoStats.getHologramsStand()) {
                    hologram.hide(e.getPlayer());
                }
            }
        }
    }

    private Double getDistanceBetween2Points(Location point1, Location point2) {
        if (!point1.getWorld().equals(point2.getWorld())) return null;
        double distance;

        double x1 = point1.getX();
        double z1 = point1.getZ();

        double x2 = point2.getX();
        double z2 = point2.getZ();

        x1 = x1 - x2;
        z1 = z1 - z2;

        if (x1 < 0) {
            x1 = -x1;
        }
        if (z1 < 0) {
            z1 = -z1;
        }

        double x = (int) x1 * x1;
        double z = (int) z1 * z1;

        distance = x + z;
        distance = Math.sqrt(distance);

        return distance;
    }

}
