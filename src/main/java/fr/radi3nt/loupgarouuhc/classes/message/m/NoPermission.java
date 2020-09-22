package fr.radi3nt.loupgarouuhc.classes.message.m;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NoPermission extends Message {

    public NoPermission() {
        PerrorMessage= ChatColor.DARK_RED + "You don't have the permission to do this";
        CerrorMessage= PREFIX_WARN + "You don't have the permission to do this";
    }

    @Override
    public void sendMessage(Player player, String comments, boolean broadcastConsole) {
        String baseText = "%err%";
        if (broadcastConsole) {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage)+ " (" + comments + ")", LoupGarouUHC.plugin.getServer().getConsoleSender());
        } else {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage) + " (" + comments + ")");
        }
        if (player!=null) {
            player.sendMessage(baseText.replace("%err%", PerrorMessage));
        }
    }

}
