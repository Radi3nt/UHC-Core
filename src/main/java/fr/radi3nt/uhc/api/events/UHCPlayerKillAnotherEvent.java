package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerKillAnotherEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final UHCGame game;
    private final UHCPlayer killer;
    private final UHCPlayer killed;
    boolean cancelled = false;

    public UHCPlayerKillAnotherEvent(UHCGame game, UHCPlayer killer, UHCPlayer killed) {
        this.game = game;
        this.killed = killed;
        this.killer = killer;
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

    public UHCPlayer getKiller() {
        return killer;
    }

    public UHCPlayer getKilled() {
        return killed;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
