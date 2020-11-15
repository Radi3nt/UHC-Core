package fr.radi3nt.uhc.api.lang.lang;

import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.Reader;
import fr.radi3nt.uhc.api.lang.json.FileUtils_;
import fr.radi3nt.uhc.api.lang.json.Json;
import fr.radi3nt.uhc.api.lang.json.JsonObject;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Language {

    public static final String DEFAULTID = "default";
    public static final String NO_MESSAGE = ChatColor.DARK_RED + "Cannot find message !" + ChatColor.RESET;
    public static final String NO_PLACEHOLDER = ChatColor.DARK_RED + "Cannot find placeholder !" + ChatColor.RESET;


    private static final ArrayList<Language> languages = new ArrayList<>();
    private final File languageFile;
    private final ArrayList<PlaceHolder> placeHolders = new ArrayList<>();
    private Map<String, String> messages = new HashMap<>();
    private final String name;
    private final String id;

    public Language(File languageFile, String name, String id) {
        this.name = name;
        this.id = id;
        this.languageFile = languageFile;
        setupLanguage();
    }

    public static ArrayList<Language> getLanguages() {
        return languages;
    }

    public String getMessage(String id) throws CannotFindMessageException {
        for (Map.Entry<String, String> message : messages.entrySet()) {
            if (message.getKey().equals(id)) {
                String messageS = ChatColor.translateAlternateColorCodes('&', message.getValue());
                for (PlaceHolder placeHolder : placeHolders) {
                    messageS = messageS.replace(placeHolder.getPlaceHolder(), placeHolder.getMessage());
                }
                return messageS;
            }
        }
        throw new CannotFindMessageException(this, id);
    }

    public String getMessage(String id, UHCPlayer player) throws CannotFindMessageException {
        return SystemPlaceHolder.getMessageReplaced(getMessage(id), player);
    }

    private void setupLanguage() {
        for (Language language : languages) {
            if (language.getId().equals(this.getId())) {
                Logger.getGeneralLogger().logInConsole(UHCCore.getPrefix() + " " + ChatColor.DARK_RED + "Invalid language: " + ChatColor.RED + getId());
                return;
            }
        }
        languages.add(this);
    }

    /*
    public void addField(String field, String value) {
        if (languageFile!=null) {
            JsonObject inputObj = new JsonObject();
            List<String> fields = Arrays.asList(field.split("\\."));
            Collections.reverse(fields);
            JsonObject lastObj = new JsonObject().add(fields.get(0), value);
            int i = 0;
            for (String s : fields) {
                if (i == fields.size()-1) {
                    inputObj.add(s, lastObj);
                    continue;
                }
                if (i != 0) {
                    lastObj = new JsonObject().add(s, lastObj);
                }
                i++;
            }
            JsonObject jsonObject = Json.parse(FileUtils_.loadContent(languageFile)).asObject();
            jsonObject.merge(inputObj);
            FileUtils_.saveJson(languageFile, jsonObject);
        }
    }

     */

    public void update() {
        if (!id.equals(DEFAULTID)) {
            Reader reader = new Reader();
            reader.loadLanguage(languageFile);
        }
    }

    public void addFile(File file) {
        if (languageFile != null) {
            if (file != null && file.exists() && file.getName().contains(".json")) {
                JsonObject origin = Json.parse(FileUtils_.loadContent(languageFile)).asObject();
                JsonObject newJson = Json.parse(FileUtils_.loadContent(file)).asObject();
                origin.merge(newJson);
                FileUtils_.saveJson(languageFile, origin);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Language)
            return this.id.equals(((Language) obj).getId());
        return false;
    }

    public ArrayList<PlaceHolder> getPlaceHolders() {
        return placeHolders;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
