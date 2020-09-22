package fr.radi3nt.loupgarouuhc.classes;

public enum Reason {
    TUÉ("§7§l%s§4 est mort"),
    GM_LOUP_GAROU("§7§l%s§4 est mort pendant la nuit"),
    CHASSEUR_DE_VAMPIRE("§7§l%s§4 s'est fait purifier"),
    VOTE("§7§l%s§4 a été victime du vote"),
    CHASSEUR("§7§l%s§4 est mort sur le coup"),
    DICTATOR("§7§l%s§4 a été désigné"),
    DICTATOR_SUICIDE("§7§l%s§4 s'est suicidé par culpabilité"),
    DISCONNECTED("§7§l%s§4 est mort d'une déconnexion"),
    LOVE("§7§l%s§4 s'est suicidé par amour"),
    BOUFFON("§7§l%s§4 est mort de peur"),
    ASSASSIN("§7§l%s§4 s'est fait poignarder"),
    PYROMANE("§7§l%s§4 est parti en fumée"),
    PIRATE("§7§l%s§4 était l'otage"),
    FAUCHEUR("§7§l%s§4 a égaré son âme"),

    DONT_DIE("§7§l%s§4 est mort pour rien");

    String message;


    Reason(String message) {
        this.message = message;
    }

}
