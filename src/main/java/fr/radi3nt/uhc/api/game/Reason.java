package fr.radi3nt.uhc.api.game;

public enum Reason {
    KILLED_BY_PLAYER("est mort"),
    KILLED_BY_MOB("est mort"),
    KILLED_UNDEFINED("est mort"),
    DISCONNECTED("est mort d'une déconnexion"),
    LOVE("s'est suicidé par amour"),

    DONT_DIE("§7§l%s§4 est mort pour rien");

    private final String message;

    Reason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
