package fr.radi3nt.uhc.api.game.reasons;

import fr.radi3nt.uhc.api.player.UHCPlayer;

public class ReasonSlainByPlayer extends ReasonSlain {

    private final UHCPlayer killer;

    public ReasonSlainByPlayer(UHCPlayer killer) {
        this.killer=killer;
    }

    @Override
    public String getMessage() {
        return "game.death.slain-player";
    }

    public UHCPlayer getKiller() {
        return killer;
    }
}
