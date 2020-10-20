package fr.radi3nt.loupgarouuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LGTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            if (commandSender.hasPermission("lg.manage"))
                list.add("manage");
            if (commandSender.hasPermission("lg.lang"))
                list.add("lang");
            if (commandSender.hasPermission("lg.start"))
                list.add("start");
            if (commandSender.hasPermission("lg.join"))
                list.add("join");
            if (commandSender.hasPermission("lg.leave"))
                list.add("leave");
            if (commandSender.hasPermission("lg.gui"))
                list.add("gui");
            if (commandSender.hasPermission("lg.stats"))
                list.add("stats");
            if (commandSender.hasPermission("lg.tell"))
                list.add("tell");
            if (commandSender.hasPermission("lg.msg"))
                list.add("msg");
            if (commandSender.hasPermission("lg.spectate"))
                list.add("spectate");
            list.add("vote");
            list.add("list");
            list.add("role");
            list.removeIf(s1 -> !s1.startsWith(strings[0]));
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("manage") && commandSender.hasPermission("lg.manage")) {
            list.add("skip");
            list.add("scatter");
            list.add("night");
            list.add("name");
            list.add("host");
            list.add("regen");
            list.add("stop");
            list.removeIf(s1 -> !s1.startsWith(strings[1]));
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("role")) {
            list.add("couple");
            list.add("see");
            list.add("lg");
            list.add("respawn");
            list.add("don");
            list.removeIf(s1 -> !s1.startsWith(strings[1]));
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("stats") && commandSender.hasPermission("lg.stats.manage")) {
            list.add("place");
            list.add("refresh");
            list.add("remove");
            list.add("movehere");
            list.removeIf(s1 -> !s1.startsWith(strings[1]));
        } else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                list.add(onlinePlayer.getName());
            }
            list.removeIf(s1 -> !s1.startsWith(strings[strings.length - 1]));
        }
        return list;
    }
}
