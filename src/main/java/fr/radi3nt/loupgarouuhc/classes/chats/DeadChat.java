package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.players;

public class DeadChat extends Chat{

    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = "";
        if (user.getRole()!=null) {
            finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "DEAD" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "[" + ChatColor.WHITE + user.getRole().getName(user.getLanguage()) + ChatColor.GRAY + "] " + user.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message;
        } else {
            finalMessage = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "DEAD" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + user.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message;
        }
        LoupGarouUHC.console.sendMessage(finalMessage);
        for (LGPlayer lgp : players) {
            if (lgp.getChat()==this) {
                lgp.sendMessage(finalMessage);
            }
        }
    }

}
