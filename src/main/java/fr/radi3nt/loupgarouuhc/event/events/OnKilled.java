package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnKilled extends Event {

    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final LGGame game;
    private final UHCPlayer killed;
    private final UHCPlayer killer;
    private final Location location;

    public OnKilled(LGGame game, UHCPlayer killed, UHCPlayer killer, Location playerloc) {
        this.game = game;
        this.killed = killed;
        this.location = playerloc;
        this.killer = killer;
    }
    public LGGame getGame() {
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
}
