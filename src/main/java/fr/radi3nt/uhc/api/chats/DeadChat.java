package fr.radi3nt.uhc.api.chats;

import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;


public class DeadChat extends Chat {

    public void sendMessage(UHCPlayer user, String message) {
        String finalMessage = "";
        finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "DEAD" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + user.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message;
        Logger.getChat().logInConsole(finalMessage);
        for (UHCPlayer lgp : UHCCore.getPlayers()) {
            if (lgp.getChat() == this) {
                lgp.sendMessage(finalMessage);
            }
        }
    }

}
