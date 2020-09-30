package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.players;

public class GeneralChat extends Chat {

    //todo change this

    @Override
    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = ChatColor.GREEN + "[" + ChatColor.GOLD + "GENERAL" + ChatColor.GREEN + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.AQUA + " » " + ChatColor.RESET + message;
        LoupGarouUHC.console.sendMessage(finalMessage);
        for (LGPlayer lgp : players) {
            lgp.sendMessage(finalMessage);
        }
    }

}
