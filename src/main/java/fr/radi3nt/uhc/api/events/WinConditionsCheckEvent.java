package fr.radi3nt.uhc.api.events;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class WinConditionsCheckEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final List<UHCPlayer> winners;
    private final UHCGame game;
    private boolean cancel;

    public WinConditionsCheckEvent(UHCGame game) {
        this.cancel = false;
        this.winners = new ArrayList<>();
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return WinConditionsCheckEvent.HANDLERS_LIST;
    }

    public HandlerList getHandlers() {
        return WinConditionsCheckEvent.HANDLERS_LIST;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    public List<UHCPlayer> getVictoryTeam() {
        return this.winners;
    }

    public UHCGame getGame() {
        return game;
    }

}
