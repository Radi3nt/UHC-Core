package fr.radi3nt.uhc.api.scenarios;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.command.commands.*;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtils;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

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

        if (strings[0].equalsIgnoreCase("start")) {
            if (UHCCore.getGames().get(0).getState() == GameState.LOBBY) {
                UHCCore.getGames().get(0).getAlivePlayers().addAll(UHCCore.getPlayers());
                UHCCore.getGames().get(0).getDeadPlayers().addAll(UHCCore.getPlayers());
                if (!UHCCore.getGames().get(0).updateStart()) {
                    UHCCore.getGames().get(0).getAlivePlayers().removeAll(UHCCore.getPlayers());
                    UHCCore.getGames().get(0).getDeadPlayers().removeAll(UHCCore.getPlayers());
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
        System.out.println(arrayList);
        return arrayList;
    }
}
