package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnNight;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnNightEvent implements Listener {

    @EventHandler
    public void OnNight(OnNight e) {
        for (LGPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
                lgp.getRole().OnNight(e.getGame(), lgp);
            }
        }
    }

}
