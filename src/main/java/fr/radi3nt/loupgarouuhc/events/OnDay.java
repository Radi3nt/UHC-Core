package fr.radi3nt.loupgarouuhc.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnDay extends Event {

    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final LGGame game;

    public OnDay(LGGame game) {
        this.game = game;
    }
    public LGGame getGame() {
        return game;
    }


}
