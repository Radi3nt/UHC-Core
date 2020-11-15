package fr.radi3nt.uhc.api.command;

import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtilis {

    private final CommandSender sender;
    private final org.bukkit.command.Command command;
    private final String label;
    private final String[] args;

    public CommandUtilis(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public static boolean requirePermission(CommandSender sender, String perm, String comments) throws NoPermissionException {
        if (sender.hasPermission(perm)) {
            return true;
        }
        throw new NoPermissionException(perm, ((Player) sender).getUniqueId(), true);
    }

    public boolean executeCommand(String name, String permission, int argsNumber, Checks... checks) throws NoPermissionException, NoArgsException {
        String commandS = command.getName();
        for (int i = 0; i < args.length; i++) {
            if (i < name.split("\\.").length - 1) {
                String arg = args[i];
                commandS += "." + arg;
            }
        }
        if (commandS.equalsIgnoreCase(name)) {
            if (permission.isEmpty() || sender.hasPermission(permission)) {
                int args = this.args.length + 1 - name.split("\\.").length;
                if (args >= argsNumber) {
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
                    throw new NoArgsException(args);
                }
            } else {
                throw new NoPermissionException("No permission for command: " + command.getName(), sender instanceof Player ? ((Player) sender).getUniqueId() : null, true);
            }
        }
        return false;
    }

    public boolean executeCommand(String[] name, String permission, int argsNumber, Checks... checks) throws NoPermissionException, NoArgsException {
        for (String s : name)
            if (executeCommand(s, permission, argsNumber, checks))
                return true;
        return false;
    }

    public boolean checkIfPlayer() {
        return sender instanceof Player;
    }

    public boolean checkIfGame() {
        if (checkIfPlayer()) {
            UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
            return lgp.isInGame();
        } else {
            return false;
        }
    }

    public boolean requireMinArgs(int minArgs) throws NoArgsException {
        if (this.getArgs().length >= minArgs) {
            return true;
        } else {
            throw new NoArgsException(minArgs - this.getArgs().length);
        }
    }

    public CommandSender getSender() {
        return sender;
    }

    public org.bukkit.command.Command getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public enum Checks {
        GAME,
        PLAYER,
        NULL,
        ;
    }
}
