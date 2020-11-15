package fr.radi3nt.loupgarouuhc.event.customlisteners;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnNight;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnNightEvent implements Listener {

    @EventHandler
    public void OnNight(OnNight e) {
        for (UHCPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.isLinkedToPlayer() && lgp.getGameData().hasRole()) {
                lgp.getGameData().getRole().OnNight(e.getGame(), lgp);
                lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, SoundCategory.AMBIENT, 1f, 1f);
            }
        }
    }

}
