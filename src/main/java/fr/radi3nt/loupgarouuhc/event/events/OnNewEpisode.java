package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnNewEpisode extends Event {

    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final LGGame game;
    private final int day;

    public OnNewEpisode(LGGame game, Integer day) {
        this.game = game;
        this.day = day;
    }
    public LGGame getGame() {
        return game;
    }
    public Integer getDay() {
        return day;
    }

}
