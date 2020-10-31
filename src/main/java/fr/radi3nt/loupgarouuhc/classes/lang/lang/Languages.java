package fr.radi3nt.loupgarouuhc.classes.lang.lang;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Languages {

    public static final String DEFAULTID = "default";
    private static final ArrayList<Languages> languages = new ArrayList<>();
    private static final String defaultMessage = ChatColor.DARK_RED + "Cannot find message !" + ChatColor.RESET;
    private static final String defaultRole = ChatColor.DARK_RED + "Cannot find message !" + ChatColor.RESET;
    private static final String defaultPlaceHolder = ChatColor.DARK_RED + "Cannot find message !" + ChatColor.RESET;
    private final ArrayList<RoleLang> roleLangs;
    private final ArrayList<Message> messages;
    private final ArrayList<PlaceHolder> placeHolders;
    private final String name;
    private final String id;

    public Languages(String name, String id, ArrayList<RoleLang> roleLangs, ArrayList<Message> messages, ArrayList<PlaceHolder> placeHolders) {
        this.roleLangs = roleLangs;
        this.messages = messages;
        this.placeHolders = placeHolders;
        this.name = name;
        this.id = id;
        setupLanguage();
    }

    public static ArrayList<Languages> getLanguages() {
        return languages;
    }

    @Deprecated
    public String replaceMessages(String message) {
        String newMessage = message;
        for (Message message1 : messages) {
            newMessage = newMessage.replace(message1.getId(), message1.getMessage());
        }
        for (PlaceHolder placeHolder : placeHolders) {
            newMessage = newMessage.replace(placeHolder.getPlaceHolder(), placeHolder.getMessage());
        }
        return newMessage;
    }

    public String getMessage(String id) {
        for (Message message : getMessages()) {
            if (message.getId().equals(id)) {
                String messageS = message.getMessage();
                for (PlaceHolder placeHolder : placeHolders) {
                    messageS = messageS.replace(placeHolder.getPlaceHolder(), placeHolder.getMessage());
                }
                return messageS;
            }
        }
        return defaultMessage;
    }

    public String getMessage(String id, LGPlayer player) {
        for (Message message : getMessages()) {
            if (message.getId().equals(id)) {
                String messageS = message.getMessage();
                for (PlaceHolder placeHolder : placeHolders) {
                    messageS = messageS.replace(placeHolder.getPlaceHolder(), placeHolder.getMessage());
                }
                return SystemPlaceHolder.getMessageReplaced(messageS, player);
            }
        }
        return defaultMessage;
    }

    public RoleLang getRoleAspect(String name) {
        for (RoleLang roleLang : getRoleAspects()) {
            if (roleLang.getName().equals(name)) {
                return roleLang;
            }
        }
        return null;
    }

    public void setupLanguage() {
        for (Languages language : languages) {
            if (language.getId().equals(this.getId())) {
                Logger.getGeneralLogger().logInConsole(LoupGarouUHC.getPrefix() + " " + ChatColor.DARK_RED + "Invalid language: " + ChatColor.RED + getId());
                return;
            }
        }
        languages.add(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Languages)
            return this.id.equals(((Languages) obj).getId());
        return false;
    }

    public ArrayList<RoleLang> getRoleAspects() {
        return roleLangs;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
