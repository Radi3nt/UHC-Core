package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnDiscoverRole;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnDiscoverRoleEvent implements Listener {

    @EventHandler
    public void OnDiscoverRole(OnDiscoverRole e) {
        for (LGPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
                lgp.getRole().OnDiscoverRole(e.getGame(), lgp);
            }
        }
    }

}
