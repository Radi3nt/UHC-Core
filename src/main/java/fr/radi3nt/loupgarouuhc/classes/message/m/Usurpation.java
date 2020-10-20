/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc.classes.message.m;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Usurpation extends Message {
    public Usurpation(String name) {
        PerrorMessage = ChatColor.DARK_RED + "You are not " + ChatColor.RED + name + ChatColor.DARK_RED;
        CerrorMessage = PREFIX_WARN + "You are not " + name;
    }

    @Override
    public void sendMessage(Player player, String comments, boolean broadcastConsole) {
        String baseText = "%err%";
        if (broadcastConsole) {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage) + " (" + comments + ")", LoupGarouUHC.plugin.getServer().getConsoleSender());
        } else {
            Logger.getLogger().log(baseText.replace("%err%", CerrorMessage) + " (" + comments + ")");
        }
        if (player != null) {
            player.sendMessage(baseText.replace("%err%", PerrorMessage));
        }
    }
}
