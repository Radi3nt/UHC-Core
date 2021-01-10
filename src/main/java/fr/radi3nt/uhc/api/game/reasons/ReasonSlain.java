package fr.radi3nt.uhc.api.game.reasons;

import fr.radi3nt.uhc.api.player.UHCPlayer;

public class ReasonSlain implements BroadcastReason {

    @Override
    public String getMessage() {
        return "game.death.slain";
    }

}
