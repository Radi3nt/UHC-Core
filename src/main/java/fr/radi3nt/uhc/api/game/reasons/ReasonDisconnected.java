package fr.radi3nt.uhc.api.game.reasons;

public class ReasonDisconnected implements BroadcastReason {
    @Override
    public String getMessage() {
        return "game.death.disconnected";
    }
}
