package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.DEFAULT_LANG_ID;

public abstract class Chat {

    public abstract void sendMessage(LGPlayer user, String message);

    public static void broadcastMessage(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
            lgp.sendMessage(message);
        }
    }

    public static void broadcastIdMessage(String messageID, ArrayList<LGPlayer> lgps) {
        Logger.getChat().log("id message: " + messageID);
        Languages language = null;
        for (Languages value : Languages.getLanguages()) {
            if (value.getId().equals(DEFAULT_LANG_ID))
                language = value;
        }
        if (language != null) {
            Logger.getChat().log("example: " + language.getMessage(messageID));
            Logger.getGeneralLogger().logInConsole(language.getMessage(messageID));
        } else {
            Logger.getGeneralLogger().logInConsole(messageID);
        }

        ArrayList<String> lgpName = new ArrayList<>();
        lgps.forEach(player -> lgpName.add(player.getName()));
        String list = lgpName.get(0);
        lgpName.remove(0);
        for (String s : lgpName) {
            list += ", " + s;
        }
        Logger.getChat().log("player concerned: " + list);
        for (LGPlayer gamePlayer : lgps) {
            gamePlayer.sendMessage(gamePlayer.getLanguage().getMessage(messageID, gamePlayer));
        }
    }

}
