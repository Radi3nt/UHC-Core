package fr.radi3nt.loupgarouuhc.classes.lang;

import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.English;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.Francais;

public enum Language {
    ENGLISH(English.id),
    FRANCAIS(Francais.id),
    ;

    private String id;

    Language(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }
}
