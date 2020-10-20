package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Chat {

    public abstract void sendMessage(LGPlayer user, String message);

    public static void broadcastMessage(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
            lgp.sendMessage(message);
        }
    }

    public static void broadcastIdMessage(String messageID, ArrayList<LGPlayer> lgps) {
        for (LGPlayer gamePlayer : lgps) {
            gamePlayer.sendMessage(gamePlayer.getLanguage().getMessage(messageID, gamePlayer));
        }
    }

}
