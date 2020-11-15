package fr.radi3nt.loupgarouuhc.event.customlisteners;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnKill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnKillEvent implements Listener {

    @EventHandler
    public void OnKillEvent(OnKill e) {
        UHCPlayer lgp = e.getKiller();
        if (lgp.isLinkedToPlayer() && lgp.getGameData().hasRole()) {
            lgp.getGameData().getRole().OnkillSomeone(e.getGame(), lgp, e.getKilled());
            lgp.getGameData().setKills(lgp.getGameData().getKills() + 1);
        }
    }

}
