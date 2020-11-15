package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.game.UHCGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCGameEndsEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UHCGame game;

    public UHCGameEndsEvent(UHCGame game) {
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public UHCGame getGame() {
        return game;
    }

}
