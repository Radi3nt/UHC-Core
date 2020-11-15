package fr.radi3nt.uhc.api.exeptions.common;

import fr.radi3nt.uhc.api.lang.lang.Language;

public class CannotFindMessageException extends Exception {

    private final Language language;

    public CannotFindMessageException(Language language, String message) {
        super(message);
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }
}
