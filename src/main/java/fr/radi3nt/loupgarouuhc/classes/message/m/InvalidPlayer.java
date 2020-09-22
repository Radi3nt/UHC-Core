package fr.radi3nt.loupgarouuhc.classes.message.m;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InvalidPlayer extends Message {

    public InvalidPlayer() {
        PerrorMessage= ChatColor.DARK_RED + "Invalid player";
        CerrorMessage= PREFIX_WARN + "Invalid player";
    }

    @Override
    public void sendMessage(Player player, String comments, boolean broadcastConsole) {
        String baseText = "%err%: " + comments;
        if (broadcastConsole) {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage), LoupGarouUHC.plugin.getServer().getConsoleSender());
        } else {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage));
        }
        if (player!=null) {
            player.sendMessage(baseText.replace("%err%", PerrorMessage));
        }
    }

}
