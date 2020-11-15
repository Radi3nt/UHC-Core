// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.loupgarouuhc.event.events;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class WinConditionsCheckEvent extends Event implements Cancellable
{
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean cancel;
    private WinType victoryTeam;
    private final LGGame game;
    
    public WinConditionsCheckEvent(LGGame game) {
        this.cancel = false;
        this.victoryTeam = WinType.NONE;
        this.game=game;
    }
    
    public HandlerList getHandlers() {
        return WinConditionsCheckEvent.HANDLERS_LIST;
    }
    
    public static HandlerList getHandlerList() {
        return WinConditionsCheckEvent.HANDLERS_LIST;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
    
    public void setCancelled(final boolean b) {
        this.cancel = b;
    }
    
    public void setVictoryTeam(final WinType victoryTeam) {
        this.victoryTeam = victoryTeam;
    }
    
    public WinType getVictoryTeam() {
        return this.victoryTeam;
    }

    public LGGame getGame() {
        return game;
    }

}
