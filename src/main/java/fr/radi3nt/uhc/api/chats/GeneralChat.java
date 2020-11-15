package fr.radi3nt.uhc.api.chats;

import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;


public class GeneralChat extends Chat {

    @Override
    public void sendMessage(UHCPlayer user, String message) {
        String finalMessage = ChatColor.GREEN + "[" + ChatColor.GOLD + "GENERAL" + ChatColor.GREEN + "] " + ChatColor.RESET + user.getPlayer().getDisplayName() + ChatColor.AQUA + " » " + ChatColor.RESET + message;
        Logger.getChat().logInConsole(finalMessage);
        for (UHCPlayer lgp : UHCCore.getPlayers()) {
            if (lgp.getChat() == this) {
                lgp.sendMessage(finalMessage);
            }
        }
    }

}
