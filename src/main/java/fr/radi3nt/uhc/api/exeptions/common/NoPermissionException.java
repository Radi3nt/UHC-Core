package fr.radi3nt.uhc.api.exeptions.common;

import java.util.UUID;

public class NoPermissionException extends Exception {

    private final UUID uuid;
    private final Boolean broadcast;

    public NoPermissionException(String message, UUID uuid, Boolean broadcast) {
        super(message);
        this.uuid = uuid;
        this.broadcast = broadcast;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Boolean getBroadcast() {
        return broadcast;
    }
}
