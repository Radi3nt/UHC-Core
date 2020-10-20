package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnEndGame extends Event {

    private static final HandlerList handlers = new HandlerList();
    LGGame game;

    public OnEndGame(LGGame game) {
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public LGGame getGame() {
        return game;
    }

}
