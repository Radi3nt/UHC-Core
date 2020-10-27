package fr.radi3nt.loupgarouuhc.exeptions.common;

import fr.radi3nt.loupgarouuhc.classes.message.messages.NoPermission;

import java.util.UUID;

public class NoPermissionException extends Exception {

    private final UUID uuid;
    private final Boolean broadcast;

    public NoPermissionException(String message, UUID uuid, Boolean broadcast) {
        super(message);
        this.uuid = uuid;
        this.broadcast = broadcast;
        new NoPermission().sendMessage(uuid, message, broadcast);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Boolean getBroadcast() {
        return broadcast;
    }
}
