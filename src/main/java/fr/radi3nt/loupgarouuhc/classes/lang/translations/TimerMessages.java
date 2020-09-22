package fr.radi3nt.loupgarouuhc.classes.lang.translations;

public enum TimerMessages {
    DEGA("", "Les dégats (sauf PVP) sont activés !"),
    PVP_SOON("", "PVP activé dans 10 minutes !"),
    PVP("", "PVP activé !"),
    FIN_EP("", "Fin Episode");

    public String ENGLISH;
    public String FRANCAIS;

    TimerMessages(String english, String francais) {
        this.ENGLISH=english;
        this.FRANCAIS=francais;
    }
}
