package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.players;

public class GeneralChat extends Chat{

    @Override
    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "GENERAL" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.AQUA + "" + ChatColor.BOLD + " » " + ChatColor.RESET + message;
        LoupGarouUHC.console.sendMessage(finalMessage);
        for (LGPlayer lgp : players) {
            lgp.sendMessage(finalMessage);
        }
    }
}
