package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnKill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnKillEvent implements Listener {

    @EventHandler
    public void OnKillEvent(OnKill e) {
        LGPlayer lgp = e.getKiller();
        if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
            lgp.getRole().OnKillSomeone(e.getGame(), lgp, e.getKilled());
            lgp.setKills(lgp.getKills()+1);
        }
    }

}
