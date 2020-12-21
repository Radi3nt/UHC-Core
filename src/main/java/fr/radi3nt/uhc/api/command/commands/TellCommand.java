package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TellCommand implements CommandArg {

    @Override
    public void onCommand(CommandUtilis utilis) throws NoPermissionException, NoArgsException {
        if (utilis.checkIfPlayer()) {
            if (CommandUtilis.requirePermission(utilis.getSender(), "uhc.tell", "")) {
                if (utilis.requireMinArgs(1)) {
                        String message = "";
                        for (int i = 0; i < utilis.getArgs().length; i++) {
                            message = message + " " + utilis.getArgs()[i];
                        }
                        UHCCore.broadcastMessage(UHCCore.getPrefix() + " " + ((Player) utilis.getSender()).getDisplayName() + ChatColor.AQUA + "" + ChatColor.BOLD + " »" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandUtilis utilis) {
        return null;
    }
}
