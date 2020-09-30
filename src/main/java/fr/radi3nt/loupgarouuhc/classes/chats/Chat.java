package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class Chat {

    public abstract void sendMessage(LGPlayer user, String message);

    public void broadcastMessage(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
            lgp.sendMessage(message);
        }
    }

}
