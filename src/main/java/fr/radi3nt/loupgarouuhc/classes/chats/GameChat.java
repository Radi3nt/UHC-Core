package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

public class GameChat extends Chat{

    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = ChatColor.GOLD + "[" + ChatColor.YELLOW + "GAME" + ChatColor.GOLD + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.GOLD + " » " + ChatColor.RESET + message;
        Logger.getChat().logInConsole(finalMessage);
        for (LGPlayer lgp : user.getGameData().getGame().getGamePlayersWithDeads()) {
            lgp.sendMessage(finalMessage);
        }
    }

}
