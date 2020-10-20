package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnNight extends Event {

    LGGame game;

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    public LGGame getGame() {
        return game;
    }

    public OnNight(LGGame game) {
        this.game = game;
    }

}
