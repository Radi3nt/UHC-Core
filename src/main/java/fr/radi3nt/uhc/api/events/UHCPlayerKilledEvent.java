package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerKilledEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final UHCGame game;
    private final UHCPlayer killed;
    private final UHCPlayer killer;
    private final Reason reason;
    private final Location location;
    private boolean cancelled = false;

    public UHCPlayerKilledEvent(UHCGame game, UHCPlayer killed, UHCPlayer killer, Reason reason, Location deathLocation) {
        this.game = game;
        this.killed = killed;
        this.location = deathLocation;
        this.killer = killer;
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

    public Location getLoc() {
        return location;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public Reason getReason() {
        return reason;
    }
}
