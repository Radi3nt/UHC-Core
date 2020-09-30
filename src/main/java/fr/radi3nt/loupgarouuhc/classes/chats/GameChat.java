package fr.radi3nt.loupgarouuhc.classes.chats;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;

public class GameChat extends Chat{

    public void sendMessage(LGPlayer user, String message) {
        String finalMessage = ChatColor.GOLD + "[" + ChatColor.YELLOW + "GAME" + ChatColor.GOLD + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.GOLD + " » " + ChatColor.RESET + message;
        LoupGarouUHC.console.sendMessage(finalMessage);
        for (LGPlayer lgp : user.getGame().getGamePlayersWithDeads()) {
            lgp.sendMessage(finalMessage);
        }
    }

}
