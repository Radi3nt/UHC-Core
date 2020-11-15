package fr.radi3nt.loupgarouuhc.commands;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command {

    CommandSender sender;
    org.bukkit.command.Command command;
    String label;
    String[] args;

    public Command(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public boolean executeCommand(String name, String permission, int argsNumber, Command.Checks... checks) throws NoPermissionException {
        String commandS = command.getName();
        for (int i = 0; i < args.length; i++) {
            if (i<name.split("\\.").length-1) {
                String arg = args[i];
                commandS += "." + arg;
            }
        }
        if (commandS.equalsIgnoreCase(name)) {
            if (sender.hasPermission(permission)) {
                if (args.length + 1 - name.split("\\.").length >= argsNumber) {
                    boolean valid = true;
                    for (Checks check : checks) {
                        if (!valid)
                            return false;
                        if (check.equals(Checks.GAME)) {
                            valid = checkIfGame();
                            continue;
                        }
                        if (check.equals(Checks.PLAYER)) {
                            valid = checkIfPlayer();
                            continue;
                        }
                    }
                    return valid;
                } else {
                    //todo invalid args
                }
            } else {
                throw new NoPermissionException("No permission for command: " + command.getName(), sender instanceof Player ? ((Player) sender).getUniqueId() : null, true);
            }
        } else {
                //todo non-existant command
            }
        return false;
    }


    public boolean executeCommand(String[] name, String permission, int argsNumber, Command.Checks... checks) throws NoPermissionException {
        for (String s : name)
            if (executeCommand(s, permission, argsNumber, checks))
                return true;
        return false;
    }


    public boolean checkIfPlayer() {
        //todo non-player message
        return sender instanceof Player;
    }

    public boolean checkIfGame() {
        if (checkIfPlayer()) {
            UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
            //todo non-ingame message;
            return lgp.isInGame();
        } else {
            return false;
        }
    }

    public enum Checks {
        GAME,
        PLAYER,
        NULL,
        ;
    }

}
