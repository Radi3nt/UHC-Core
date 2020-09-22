package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnKilled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnKilledEvent implements Listener {

    @EventHandler
    public void OnKilledEvent(OnKilled e) {
        LGPlayer lgp = e.getKilled();
        if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
            lgp.getRole().OnKilled(e.getGame(), e.getKilled(), e.getKiller(), e.getLoc());
        }
    }

}
