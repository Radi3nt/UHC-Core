package fr.radi3nt.uhc.api.utilis;

public class ConfigData {

    static boolean shouldCheckForUpdates = true;

    public static boolean shouldCheckForUpdates() {
        return shouldCheckForUpdates;
    }
}
