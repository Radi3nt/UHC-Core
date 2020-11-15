package fr.radi3nt.uhc.api.chats;

import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public abstract class Chat {

    public static void broadcastMessage(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(onlinePlayer);
            lgp.sendMessage(message);
        }
    }

    public static void broadcastIdMessage(String messageID, UHCPlayer[] players) {
        Logger.getChat().log("id message: " + messageID);
        Language language = null;
        for (Language value : Language.getLanguages()) {
            if (value.getId().equals(UHCCore.DEFAULT_LANG_ID))
                language = value;
        }
        try {
            if (language != null) {
                Logger.getChat().log("example: " + language.getMessage(messageID));
                Logger.getGeneralLogger().logInConsole(language.getMessage(messageID));
            } else {
                Logger.getGeneralLogger().logInConsole(messageID);
            }
        } catch (CannotFindMessageException e) {

        }

        ArrayList<String> lgpName = new ArrayList<>();
        for (UHCPlayer player : players) {
            lgpName.add(player.getName());
        }
        if (!lgpName.isEmpty()) {
            String list = lgpName.get(0);
            lgpName.remove(0);
            for (String s : lgpName) {
                list += ", " + s;
            }
            Logger.getChat().log("player concerned: " + list);
        }
        for (UHCPlayer player : players) {
            if (player != null) {
                try {
                    player.sendMessage(player.getLanguage().getMessage(messageID, player));
                } catch (CannotFindMessageException e) {
                    player.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());
                }
            }
        }
    }

    public abstract void sendMessage(UHCPlayer user, String message);

}
