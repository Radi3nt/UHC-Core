package fr.radi3nt.loupgarouuhc.classes.lang.translations.lang;

import org.bukkit.ChatColor;

public class PlaceHolder {

    private static final char idChar = '$';

    private final String id;
    private final String message;

    public PlaceHolder(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getPlaceHolder() {
        return idChar + id + idChar;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
