package fr.radi3nt.uhc.api.lang;

import fr.radi3nt.uhc.api.lang.json.FileUtils_;
import fr.radi3nt.uhc.api.lang.json.Json;
import fr.radi3nt.uhc.api.lang.json.JsonObject;
import fr.radi3nt.uhc.api.lang.json.JsonValue;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.lang.lang.PlaceHolder;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Reader {


    public void loadLanguage(File file) {
        String name;
        String id;
        HashMap<String, String> messages = new HashMap<>(loadTranslations(FileUtils_.loadContent(file)));
        name = messages.get("Language.name");
        id = messages.get("Language.id");

        if (!id.equals(Language.DEFAULTID)) {
            Language language = new Language(file, name, id);
            language.setMessages(messages);
            for (Map.Entry<String, String> stringStringEntry : messages.entrySet()) {
                if (stringStringEntry.getKey().startsWith("placeholders.")) {
                    language.getPlaceHolders().add(new PlaceHolder(stringStringEntry.getKey().replace("placeholders.", ""), stringStringEntry.getValue()));
                }
            }
            Logger.getGeneralLogger().log(ChatColor.DARK_RED + "[Language] Loading new language: " + file.getName());
        }
    }

    public boolean loadAllLanguage() {
        File[] filesList = getLangFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (!file.isHidden() && file.getName().contains(".json")) {
                    loadLanguage(file);
                } else {
                    Logger.getGeneralLogger().log(ChatColor.DARK_RED + "[Language] Skipping loading of: " + file.getName());
                }
            }
        }
        return true;
    }

    public void copyLocalToLang() {
        URI uri = null;
        try {
            uri = UHCCore.class.getResource("/langs").toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Map<String, String> env = new HashMap<>();
        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            for (Path path : zipfs.getRootDirectories()) {
                for (Path path1 : Files.list(path.resolve("/langs")).collect(Collectors.toList())) {
                    try {
                        File directoryPath = new File(UHCCore.getPlugin().getDataFolder() + "/langs/" + path1.getFileName().toString());
                        Files.copy(path1, directoryPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File[] getLangFiles() {
        File directoryPath = new File(UHCCore.getPlugin().getDataFolder() + "/langs");
        return directoryPath.listFiles();
    }


    public Map<String, String> loadTranslations(final String file) {
        final JsonObject jsonObject = Json.parse(file).asObject();
        return this.loadTranslationsRec("", jsonObject, new HashMap<String, String>());
    }

    private Map<String, String> loadTranslationsRec(final String currentPath, final JsonValue jsonValue, final Map<String, String> keys) {
        if (jsonValue.isObject()) {
            for (final JsonObject.Member member : jsonValue.asObject()) {
                final String newPath = String.format("%s%s%s", currentPath, currentPath.equals("") ? "" : ".", member.getName());
                this.loadTranslationsRec(newPath, member.getValue(), keys);
            }
        } else if (!jsonValue.isNull()) {
            keys.put(currentPath, jsonValue.asString());
        }
        return keys;
    }
}
