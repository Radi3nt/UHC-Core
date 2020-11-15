package fr.radi3nt.uhc.api.chats;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.ChatColor;

public class GameChat extends Chat{

    public void sendMessage(UHCPlayer user, String message) {
        String finalMessage = ChatColor.GOLD + "[" + ChatColor.YELLOW + "GAME" + ChatColor.GOLD + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.GOLD + " » " + ChatColor.RESET + message;
        Logger.getChat().logInConsole(finalMessage);
        UHCGame game = user.getGameData().getGame();
        for (UHCPlayer lgp : user.getGameData().getGame().getDeadAndAlivePlayers()) {
            if (lgp.isInGame())
                if (lgp.getGameData().getGame().equals(game)) {
                    lgp.sendMessage(finalMessage);
                }
        }
    }

}
