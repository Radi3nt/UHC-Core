package fr.radi3nt.uhc.api.events.stats;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.stats.Stats;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class StatsPointChanges extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final UHCPlayer player;
    private final int initialValue;
    private int newValue;
    private boolean cancel;

    public StatsPointChanges(UHCPlayer player, int newValue) {
        this.cancel = false;
        this.player=player;
        this.initialValue=player.getStats().getPoints();
        this.newValue=newValue;
    }

    public static HandlerList getHandlerList() {
        return StatsPointChanges.HANDLERS_LIST;
    }

    public HandlerList getHandlers() {
        return StatsPointChanges.HANDLERS_LIST;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    public UHCPlayer getPlayer() {
        return player;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }
}
