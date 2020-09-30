package fr.radi3nt.loupgarouuhc.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.events.OnDay;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnDayEvent implements Listener {

    @EventHandler
    public void OnDayEvent(OnDay e) {
        for (LGPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.getPlayer()!=null && lgp.getRole()!=null) {
                lgp.getRole().OnDay(e.getGame(), lgp);
                lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, SoundCategory.AMBIENT, 1f, 2f);
            }
        }
    }

}
