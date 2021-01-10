package fr.radi3nt.uhc.api.game.reasons;

public class ReasonUndefined implements BroadcastReason{
    @Override
    public String getMessage() {
        return "game.death.death";
    }
}
