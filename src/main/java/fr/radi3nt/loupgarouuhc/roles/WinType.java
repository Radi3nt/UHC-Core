package fr.radi3nt.loupgarouuhc.roles;

public enum WinType {
    VILLAGE("Le village a gagné"),
    LOUP_GAROU("Les loups garous ont gagnés"),
    SOLO("Le solo a gagné"),
    COUPLE("le couple a gagné"),
    EQUAL("Égalité!"),
    NONE( "Personne n'a gagné");

    private final String message;

    WinType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
