package fr.radi3nt.loupgarouuhc.classes.message;

import org.bukkit.ChatColor;

import java.util.UUID;

public abstract class Message {

    public static String PerrorMessage;
    public static String CerrorMessage;

    public static final String PREFIX_INFOS = ChatColor.DARK_GREEN + "[INFOS] ";
    public static final String PREFIX_WARN = ChatColor.GOLD + "[WARN] ";
    public static final String PREFIX_ERRORS = ChatColor.RED + "[ERROR] ";
    public static final String PREFIX_FATAL = ChatColor.DARK_RED + "[FATAL ERROR] ";


    public abstract void sendMessage(UUID uuid, String comments, boolean broadcastConsole);



}
