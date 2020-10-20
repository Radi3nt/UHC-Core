package fr.radi3nt.loupgarouuhc.event.customlisteners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnNewEpisode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnNewEpisodeEvent implements Listener {

    @EventHandler
    public void OnNewEpisodeEvent(OnNewEpisode e) {
        for (LGPlayer lgp : e.getGame().getGamePlayers()) {
            if (lgp.isInGame() && lgp.getGameData().hasRole()) {
                lgp.getGameData().getRole().OnNewEpisode(e.getGame(), lgp);
            }
        }
    }

}
