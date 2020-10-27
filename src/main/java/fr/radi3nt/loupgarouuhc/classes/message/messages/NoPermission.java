package fr.radi3nt.loupgarouuhc.classes.message.messages;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class NoPermission extends Message {

    public NoPermission() {
        PerrorMessage= ChatColor.DARK_RED + "You don't have the permission to do this";
        CerrorMessage= PREFIX_WARN + "You don't have the permission to do this";
    }

    @Override
    public void sendMessage(UUID uuid, String comments, boolean broadcastConsole) {
        String baseText = "%err%";
        if (broadcastConsole) {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage) + " (" + comments + ")", LoupGarouUHC.getPlugin().getServer().getConsoleSender());
        } else {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage) + " (" + comments + ")");
        }
        if (uuid != null) {
            Bukkit.getOfflinePlayer(uuid).getPlayer().sendMessage(baseText.replace("%err%", PerrorMessage));
        }
    }

}
