package fr.radi3nt.loupgarouuhc.utilis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Config {

    private static final ArrayList<Config> configs = new ArrayList<>();

    private FileConfiguration configuration;
    private File file;

    public Config(FileConfiguration configuration, File file) {
        this.configuration = configuration;
        this.file = file;
        configs.add(this);
    }

    public static Config theConfig (String path, String name) {
        for (Config config : configs) {
            if (config.getConfiguration().getCurrentPath().equals(path) && config.getConfiguration().getName().equals(name)) {
                return config;
            }
        }
        return null;
    }

    public static Config getConfig (String path, String name) {
        for (Config config : configs) {
            if (config.getConfiguration().getCurrentPath().equals(path) && config.getConfiguration().getName().equals(name)) {
                return config;
            }
        }
        File file = new File(path, name);
        if (file.exists()) {
            return createConfig(path, name);
        }
        return null;
    }

    public static Config createConfig (String path, String name) {
        for (Config config : configs) {
            if (config.getConfiguration().getCurrentPath().equals(path) && config.getConfiguration().getName().equals(name)) {
                return config;
            }
        }
        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Config(configuration, file);
    }

    public boolean saveConfig() {
        try {
            configuration.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
