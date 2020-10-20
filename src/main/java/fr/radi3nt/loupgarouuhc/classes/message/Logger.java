package fr.radi3nt.loupgarouuhc.classes.message;

import fr.radi3nt.loupgarouuhc.utilis.Config;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Logger {

    private static Logger logger;

    private final Config config;
    private boolean debug = false;

    public Logger(String path, String name) {
        config = Config.createConfig(path, name);
        logger = this;
    }

    public Logger(Config config) {
        this.config = config;
        logger = this;
    }

    public void log(String text, ConsoleCommandSender console) {
        ArrayList<String> logs = (ArrayList<String>) config.getConfiguration().getStringList("Logs");
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        logs.add("[" + hour + ":" + minute + ":" + second + "] " + text);
        config.getConfiguration().set("Logs", logs);
        config.saveConfig();
        console.sendMessage(text);
    }
    public void log(String text) {
        ArrayList<String> logs = (ArrayList<String>) config.getConfiguration().getStringList("Logs");
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        logs.add("[" + hour + ":" + minute + ":" + second + "] " + text);
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
            File file = new File(config.getFile().getPath() + name + i + ".yml");
            if (file.exists()) {
                i++;
            } else {
                found = true;
            }
        }
        Config config1 = Config.createConfig(config.getFile().getPath(), name + i + ".yml");
        config1.getConfiguration().set("Logs", logs);
        config1.saveConfig();
    }

    public void logWhenDebug(String text, ConsoleCommandSender console) {
        if (debug) {
            ArrayList<String> logs = (ArrayList<String>) config.getConfiguration().getStringList("Logs");
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int minute = now.getMinute();
            int second = now.getSecond();
            logs.add("[" + hour + ":" + minute + ":" + second + "] " + text);
            config.getConfiguration().set("Logs", logs);
            config.saveConfig();
            console.sendMessage(text);
        }
    }

    public Config getConfig() {
        return config;
    }

    public static Logger getLogger() {
        return logger;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
