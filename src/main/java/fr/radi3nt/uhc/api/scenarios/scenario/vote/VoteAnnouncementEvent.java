// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.scenarios.scenario.vote;

import fr.radi3nt.uhc.api.game.UHCGame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteAnnouncementEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final UHCGame game;
    private final Vote[] votes;
    private final PlayerVote scenario;
    private boolean cancelled = false;

    public VoteAnnouncementEvent(UHCGame game, Vote[] votes, PlayerVote scenario) {
        this.game = game;
        this.votes = votes;
        this.scenario = scenario;
    }

    public static HandlerList getHandlerList() {
        return VoteAnnouncementEvent.HANDLERS_LIST;
    }

    public HandlerList getHandlers() {
        return VoteAnnouncementEvent.HANDLERS_LIST;
    }

    public UHCGame getGame() {
        return game;
    }

    public Vote[] getVotes() {
        return votes;
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
