package fr.radi3nt.uhc.api.game;

public class DisconnectParameters {

    private int disconnectTimeout = 10;
    private boolean reconnectInPvp = true;

    public int getDisconnectTimeout() {
        return disconnectTimeout;
    }

    public void setDisconnectTimeout(int disconnectTimeout) {
        this.disconnectTimeout = disconnectTimeout;
    }

    public boolean canReconnectInPvp() {
        return reconnectInPvp;
    }

    public void setReconnectInPvp(boolean reconnectInPvp) {
        this.reconnectInPvp = reconnectInPvp;
    }
}
