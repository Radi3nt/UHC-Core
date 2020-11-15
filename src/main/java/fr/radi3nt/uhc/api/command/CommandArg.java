package fr.radi3nt.uhc.api.command;

import fr.radi3nt.uhc.api.exeptions.common.*;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandArg {

    public List<String> onTabComplete(CommandUtilis utilis) {
        List<String> list = tabComplete(utilis);
        if (list != null) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public void runCommand(CommandUtilis utilis) {
        try {
            onCommand(utilis);
        } catch (NoPermissionException e) {
            if (utilis.checkIfPlayer())
                UHCPlayer.thePlayer((Player) utilis.getSender()).sendIdMessage("commands.no.perm");
        } catch (NoCommandException e) {
            if (utilis.checkIfPlayer())
                UHCPlayer.thePlayer((Player) utilis.getSender()).sendIdMessage("commands.no.command");
        } catch (NoArgsException e) {
            if (utilis.checkIfPlayer()) {
                try {
                    String message = UHCPlayer.thePlayer((Player) utilis.getSender()).getLanguage().getMessage("commands.no.args");
                    message = String.format(message, e.getArgs());
                    utilis.getSender().sendMessage(message);
                } catch (CannotFindMessageException cannotFindMessageException) {
                    utilis.getSender().sendMessage(Language.NO_MESSAGE);
                }
            }
        } catch (NoUHCPlayerException e) {
            utilis.getSender().sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + " You must be a player to execute that command");
        }
    }

    protected abstract void onCommand(CommandUtilis utilis) throws NoPermissionException, NoCommandException, NoArgsException, NoUHCPlayerException;

    protected abstract List<String> tabComplete(CommandUtilis utilis);

}
