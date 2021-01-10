package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UHCPlayer player;

    public UHCPlayerJoinEvent(UHCPlayer player) {
        this.player = player;
    }

    public UHCPlayer getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
