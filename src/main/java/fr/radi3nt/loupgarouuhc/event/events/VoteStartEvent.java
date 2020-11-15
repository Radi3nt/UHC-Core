// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVote;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteStartEvent extends Event
{
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final UHCGame game;
    private final PlayerVote scenario;

    public VoteStartEvent(UHCGame game, PlayerVote scenario) {
        this.game=game;
        this.scenario = scenario;
    }
    
    public HandlerList getHandlers() {
        return VoteStartEvent.HANDLERS_LIST;
    }
    
    public static HandlerList getHandlerList() {
        return VoteStartEvent.HANDLERS_LIST;
    }

    public UHCGame getGame() {
        return game;
    }

    public PlayerVote getScenario() {
        return scenario;
    }
}
