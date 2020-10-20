/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Hologram;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDie implements Listener {


    @EventHandler
    public void OnPlayerDie(PlayerDeathEvent e) {
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            for (Hologram hologram : holoStats.getHologramsStand()) {
                hologram.hide(e.getEntity());
            }
        }
    }
}
