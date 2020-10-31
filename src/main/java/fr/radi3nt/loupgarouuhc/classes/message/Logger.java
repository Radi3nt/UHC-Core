package fr.radi3nt.loupgarouuhc.classes.message;

import fr.radi3nt.loupgarouuhc.utilis.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Logger {

    private static Logger general;
    private static Logger chat;
    private static Logger game;
    private final ConsoleCommandSender console;

    private final Config config;
    private boolean debug = false;

    public Logger(String path, String name, ConsoleCommandSender console) {
        config = Config.createConfig(path, name);
        this.console = console;
    }

    public Logger(Config config, ConsoleCommandSender console) {
        this.config = config;
        this.console = console;
    }

    public static Logger getChat() {
        return chat;
    }

    public static void setChat(Logger chat) {
        Logger.chat = chat;
    }

    public static Logger getGame() {
        return game;
    }

    public static void setGame(Logger game) {
        Logger.game = game;
    }

    public static Logger getGeneralLogger() {
        return general;
    }

    public static void setGeneralLogger(Logger logger) {
        Logger.general = logger;
    }

    public void log(String text) {
        logInFile(text);
    }

    public void logInConsole(String text) {
        log(text);
        console.sendMessage(text);
    }

    public void log(Exception e) {
        logInFile("/!\\ This is an error stack trace, please report it /!\\");
        logInFile("Error: " + e.toString());
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace)
            logInFile("\tat " + traceElement);
    }

    public Config getConfig() {
        return config;
    }

    private void logInFile(String text) {
        ArrayList<String> logs = (ArrayList<String>) config.getConfiguration().getStringList("Logs");
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        logs.add("[" + hour + ":" + minute + ":" + second + "] " + ChatColor.stripColor(text));
        config.getConfiguration().set("Logs", logs);
        config.saveConfig();
    }

    public void archive() {
        ArrayList<String> logs = (ArrayList<String>) config.getConfiguration().getStringList("Logs");
        LocalDateTime now = LocalDateTime.now();
        String name = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "-";
        boolean found = false;
        int i = 1;
        while (!found) {
            File file = new File(config.getFile().getParent() + "/" + name + i + ".yml");
            if (file.exists()) {
                i++;
            } else {
                found = true;
            }
        }
        Config config1 = Config.createConfig(config.getFile().getParent(), name + i + ".yml");
        config1.getConfiguration().set("Logs", logs);
        config1.saveConfig();
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
