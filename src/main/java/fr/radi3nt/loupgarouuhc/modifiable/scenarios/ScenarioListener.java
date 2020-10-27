package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioUtilis;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ScenarioListener implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onEvent(Event e) {
        ScenarioUtilis.callEvent(e);
    }

}
