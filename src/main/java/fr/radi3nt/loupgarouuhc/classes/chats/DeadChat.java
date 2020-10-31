package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPlayers;

public class DeadChat extends Chat{

    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = "";
        if (user.getGameData().hasRole()) {
            finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "DEAD" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "[" + ChatColor.WHITE + user.getGameData().getRole().getName(user.getLanguage()) + ChatColor.GRAY + "] " + user.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message;
        } else {
            finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "DEAD" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + user.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message;
        }
        Logger.getChat().logInConsole(finalMessage);
        for (LGPlayer lgp : getPlayers()) {
            if (lgp.getChat() == this) {
                lgp.sendMessage(finalMessage);
            }
        }
    }

}
