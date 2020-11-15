package fr.radi3nt.uhc.uhc.listeners;

import fr.radi3nt.uhc.api.events.WinConditionsCheckEvent;
import fr.radi3nt.uhc.uhc.ClassicGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ClassicGameCheck implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWinCheck(WinConditionsCheckEvent e) {
        if (!e.isCancelled()) {
            if (e.getGame() instanceof ClassicGame)
                if (e.getGame().getAlivePlayers().size() <= 1) {
                    e.getVictoryTeam().addAll(e.getGame().getAlivePlayers());
                    e.setCancelled(true);
                }
        }
    }

}
