package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class StatsCommand implements CommandArg {
    @Override
    public void onCommand(CommandUtilis utilis) throws NoPermissionException {
        if (utilis.getArgs().length > 0) {
            if (requirePermission(utilis.getSender(), "uhc.stats.manage", "")) {
                switch (utilis.getArgs()[0]) {
                    case "place":
                        if (utilis.checkIfPlayer())
                            if (requirePermission(utilis.getSender(), "uhc.stats.place", ""))
                                HoloStats.createHoloStats(((Player) utilis.getSender()).getLocation(), 9);
                        break;

                    case "remove":

                        break;

                    case "movehere":

                        break;

                    case "refresh":
                        HoloStats.updateAll();
                        break;

                    default:
                        utilis.getSender().sendMessage(UHCCore.getPrefix() + ChatColor.RED + " Command inconnue !");
                        break;
                }
            }
        } else if (requirePermission(utilis.getSender(), "uhc.stats", "")) {
            if (utilis.checkIfPlayer()) {
                utilis.getSender().sendMessage(ChatColor.BOLD + "STATS:\n");
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(' ');
                DecimalFormat df = new DecimalFormat("###,###", symbols);
                //DecimalFormat df = new DecimalFormat("###,###.00", symbols);
                UHCPlayer player = UHCPlayer.thePlayer((Player) utilis.getSender());
                String pointsS = df.format(player.getStats().getPoints());
                utilis.getSender().sendMessage(ChatColor.GRAY + "- Points: " + ChatColor.RED + ChatColor.BOLD + pointsS);
                utilis.getSender().sendMessage(ChatColor.GRAY + "- Nombre de game: " + ChatColor.RED + ChatColor.BOLD + player.getStats().getGameNumber());
                utilis.getSender().sendMessage(ChatColor.GRAY + "- Nombre de game gagné: " + ChatColor.RED + ChatColor.BOLD + player.getStats().getWinnedGames());
                utilis.getSender().sendMessage(ChatColor.GRAY + "- Ratio de win (" + ChatColor.GREEN + "game gagné" + ChatColor.GOLD + "/" + ChatColor.GREEN + "game" + ChatColor.GRAY + ") : " + ChatColor.RED + ChatColor.BOLD + (Double.isNaN(player.getStats().getPercentageOfWin()) ? "NaN" : player.getStats().getPercentageOfWin()));
                utilis.getSender().sendMessage(ChatColor.GRAY + "- Kills: " + ChatColor.RED + ChatColor.BOLD + player.getStats().getKills());
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandUtilis utilis) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("place");
        arrayList.add("remove");
        arrayList.add("movehere");
        arrayList.add("refresh");
        arrayList.removeIf(s1 -> !s1.startsWith(utilis.getArgs()[0]));
        return arrayList;
    }
}
