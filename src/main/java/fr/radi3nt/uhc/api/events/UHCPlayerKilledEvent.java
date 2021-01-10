package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.game.reasons.Reason;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerKilledEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UHCGame game;
    private final UHCPlayer killed;
    private final Reason reason;
    private boolean proceeded = false;

    public UHCPlayerKilledEvent(UHCGame game, UHCPlayer killed, Reason reason) {
        this.game = game;
        this.killed = killed;
        this.reason = reason;
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

    public UHCPlayer getKilled() {
        return killed;
    }

    public Reason getReason() {
        return reason;
    }

    public boolean isProceeded() {
        return proceeded;
    }

    public void setProceeded(boolean proceeded) {
        this.proceeded = proceeded;
    }
}
