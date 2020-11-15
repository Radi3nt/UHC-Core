package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnKill extends Event {

    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final LGGame game;
    private final UHCPlayer killer;
    private final UHCPlayer killed;

    public OnKill(LGGame game, UHCPlayer killer, UHCPlayer killed) {
        this.game = game;
        this.killed = killed;
        this.killer = killer;
    }
    public LGGame getGame() {
        return game;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    public UHCPlayer getKilled() {
        return killed;
    }
}
