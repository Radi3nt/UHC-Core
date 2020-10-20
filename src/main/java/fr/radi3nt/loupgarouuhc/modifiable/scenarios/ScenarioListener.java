package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class ScenarioListener implements Listener {


    public void onEvent(Event e) {
        Scenario.callEvent(e);
    }

}
