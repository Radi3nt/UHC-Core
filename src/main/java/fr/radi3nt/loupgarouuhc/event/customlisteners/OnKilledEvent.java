package fr.radi3nt.loupgarouuhc.event.customlisteners;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnKilled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnKilledEvent implements Listener {

    @EventHandler
    public void OnKilledEvent(OnKilled e) {
        UHCPlayer lgp = e.getKilled();
        if (lgp.isLinkedToPlayer() && lgp.getGameData().hasRole()) {
            lgp.getGameData().getRole().OnKilled(e.getGame(), e.getKilled(), e.getKiller(), e.getLoc());
        }
    }

}
