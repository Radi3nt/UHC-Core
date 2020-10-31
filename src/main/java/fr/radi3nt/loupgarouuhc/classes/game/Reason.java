package fr.radi3nt.loupgarouuhc.classes.game;

public enum Reason {
    TUÉ("est mort"),
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
