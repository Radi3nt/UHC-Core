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

public class MsgCommand extends CommandArg {

    @Override
    protected void onCommand(CommandUtilis utilis) throws NoPermissionException, NoArgsException {
        if (utilis.checkIfPlayer()) {
            if (CommandUtilis.requirePermission(utilis.getSender(), "uhc.msg", "")) {
                if (utilis.requireMinArgs(1)) {
                    UHCPlayer target = UHCPlayer.thePlayer(utilis.getArgs()[0]);
                    if (target != null && target.getPlayer() != utilis.getSender()) {
                        String message = "";
                        for (int i = 1; i < utilis.getArgs().length; i++) {
                            message = message + " " + utilis.getArgs()[i];
                        }
                        String msg = ChatColor.AQUA + "[" + ChatColor.GOLD + "me" + ChatColor.RED + " -> " + ChatColor.GOLD + target.getName() + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                        String msg1 = ChatColor.AQUA + "[" + ChatColor.GOLD + ((Player) utilis.getSender()).getPlayerListName() + ChatColor.RED + " -> " + "me" + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                        String msg2 = ChatColor.AQUA + "[" + ChatColor.GOLD + ((Player) utilis.getSender()).getPlayerListName() + ChatColor.RED + " -> " + ChatColor.GOLD + target.getName() + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                        target.sendMessage(msg1);
                        utilis.getSender().sendMessage(msg);
                        if (UHCPlayer.thePlayer((Player) utilis.getSender()).isInGame() || target.isInGame()) {
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (onlinePlayer.hasPermission("uhc.admin")) {
                                    onlinePlayer.sendMessage(UHCCore.getPrefix() + ChatColor.BLUE + " [Admin] " + msg2);
                                }
                                if (target.isInGame() && UHCPlayer.thePlayer(onlinePlayer).equals(target.getGameData().getGame().getData().getHost())) {
                                    onlinePlayer.sendMessage(UHCCore.getPrefix() + ChatColor.BLUE + " [Host] " + msg2);
                                } else if (UHCPlayer.thePlayer((Player) utilis.getSender()).isInGame() && UHCPlayer.thePlayer(onlinePlayer).equals(UHCPlayer.thePlayer((Player) utilis.getSender()).getGameData().getGame().getData().getHost())) {
                                    onlinePlayer.sendMessage(UHCCore.getPrefix() + ChatColor.BLUE + " [Host] " + msg2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandUtilis utilis) {
        List<String> list = new ArrayList<>();
        if (utilis.getArgs().length == 1) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                list.add(onlinePlayer.getName());
            }
        }
        return list;
    }
}
