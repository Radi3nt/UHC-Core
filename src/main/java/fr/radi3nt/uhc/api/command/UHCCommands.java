package fr.radi3nt.uhc.api.command;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.command.commands.*;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtils;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class UHCCommands implements CommandExecutor, TabCompleter {

    private static final Map<String, CommandArg> commands = new HashMap<>();

    static {
        commands.put("h", new HelpCommand());
        commands.put("help", new HelpCommand());
        commands.put("stats", new StatsCommand());
        commands.put("stat", new StatsCommand());
        commands.put("s", new StatsCommand());
        commands.put("lang", new LangCommand());
        commands.put("langs", new LangCommand());
        commands.put("l", new LangCommand());
        commands.put("manage", new ManageCommand());
        commands.put("m", new ManageCommand());
        commands.put("scenarios", new ScenariosCommands());
        commands.put("scenario", new ScenariosCommands());
        commands.put("msg", new MsgCommand());
        commands.put("whisper", new MsgCommand());
        commands.put("tell", new TellCommand());
        commands.put("game", new GameCommand());
        commands.put("spec", new SpectateCommand());
        commands.put("spectate", new SpectateCommand());
    }

    public static Map<String, CommandArg> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commands.get("h").runCommand(new CommandUtilis(commandSender, command, s, strings));
            return true;
        }

        String[] newArgs = new String[strings.length - 1];
        System.arraycopy(strings, 1, newArgs, 0, strings.length - 1);
        CommandUtilis newCommandUtilis = new CommandUtilis(commandSender, command, s, newArgs);
        CommandUtilis oldCommandUtilis = new CommandUtilis(commandSender, command, s, strings);

        if (strings[0].equalsIgnoreCase("start")) {
            if (UHCCore.getGameQueue().get(0).getState() == GameState.LOBBY) {
                if (UHCCore.getGameQueue().get(0).getWaitQueue().isEmpty()) {
                    UHCCore.getGameQueue().get(0).getWaitQueue().addAll(UHCCore.getPlayers());
                    if (!UHCCore.getGameQueue().get(0).updateStart()) {
                        UHCCore.getGameQueue().get(0).getWaitQueue().removeAll(UHCCore.getPlayers());
                    }
                } else {
                    UHCCore.getGameQueue().get(0).updateStart();
                }
            }
        } else if (strings[0].equalsIgnoreCase("leave")) {
            if (commandSender instanceof Player) {
                try {
                    if (requirePermission(commandSender, "uhc.leave", "")) {
                        if (UHCCore.getGameQueue().get(0).getAlivePlayers().contains(UHCPlayer.thePlayer((Player) commandSender))) {
                            if (UHCCore.getGameQueue().get(0).getState()==GameState.LOBBY) {
                                UHCCore.getGameQueue().get(0).getAlivePlayers().remove(UHCPlayer.thePlayer((Player) commandSender));
                                commandSender.sendMessage(UHCCore.getPrefix() + ChatColor.GOLD + " Leaved game");
                            }
                        }
                    }
                } catch (NoPermissionException e) {
                    if (oldCommandUtilis.checkIfPlayer())
                        UHCPlayer.thePlayer((Player) commandSender).sendIdMessage("commands.no.command");
                }
            }
        } else if (commands.containsKey(strings[0])) {
            commands.get(strings[0]).runCommand(newCommandUtilis);
        } else {
            ScenarioUtils.callCommand(new CommandUtilis(commandSender, command, s, strings));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (strings.length == 1) {
            arrayList.addAll(commands.keySet());
            arrayList.removeIf(s1 -> !s1.startsWith(strings[0]));
        }
        if (strings.length > 1) {
            String[] newArgs = new String[strings.length - 1];
            System.arraycopy(strings, 1, newArgs, 0, strings.length - 1);
            CommandUtilis newCommandUtilis = new CommandUtilis(commandSender, command, s, newArgs);

            for (String s1 : commands.keySet()) {
                if (strings[0].equalsIgnoreCase(s1)) {
                    arrayList.addAll(commands.get(s1).onTabComplete(newCommandUtilis));
                }
            }
        }
        Collections.sort(arrayList);
        if (arrayList.isEmpty())
            return null;
        return arrayList;
    }
}
