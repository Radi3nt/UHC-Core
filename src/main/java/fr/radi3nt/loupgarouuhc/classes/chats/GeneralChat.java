package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;


public class GeneralChat extends Chat {

    //todo general chat not seen by game

    @Override
    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = ChatColor.GREEN + "[" + ChatColor.GOLD + "GENERAL" + ChatColor.GREEN + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.AQUA + " » " + ChatColor.RESET + message;
        Logger.getChat().logInConsole(finalMessage);
        for (LGPlayer lgp : LoupGarouUHC.getPlayers()) {
            lgp.sendMessage(finalMessage);
        }
    }

}
