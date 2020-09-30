package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnDiscoverRole;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnDiscoverRoleEvent implements Listener {

    @EventHandler
    public void OnDiscoverRole(OnDiscoverRole e) {
        for (LGPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
                lgp.getRole().OnDiscoverRole(e.getGame(), lgp);
                lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.AMBIENT, 1f, 1f);
            }
        }
    }

}
