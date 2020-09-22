package fr.radi3nt.loupgarouuhc.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
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
    private final LGPlayer killer;
    private final LGPlayer killed;

    public OnKill(LGGame game, LGPlayer killer, LGPlayer killed) {
        this.game = game;
        this.killed = killed;
        this.killer = killer;
    }
    public LGGame getGame() {
        return game;
    }

    public LGPlayer getKiller() {
        return killer;
    }

    public LGPlayer getKilled() {
        return killed;
    }
}
