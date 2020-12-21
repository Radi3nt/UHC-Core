package fr.radi3nt.uhc.api.utilis;

import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.player.npc.NMSBase;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;

public class ScoreboardsUtil {


    public static void sendDefaultTabToPlayer(UHCPlayer player) {
        try {
            Object packet = NMSBase.getNMSClass("PacketPlayOutPlayerListHeaderFooter").newInstance();
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            Object header = new ChatComponentText(ChatColor.YELLOW + "☰☰☰☰☰ " + ChatColor.GOLD + ChatColor.BOLD + "Plugin UHC Core" + ChatColor.YELLOW + " ☰☰☰☰☰");
            Object footer = new ChatComponentText(ChatColor.GOLD + "In lobby" + "\n" + "\n" + ChatColor.AQUA + "Waiting for a game" + ChatColor.RESET + " --- " + ChatColor.GRAY + player.getStats().getPoints() + " points" + "\n" + ChatColor.DARK_GREEN + "Plugin par Radi3nt" + ChatColor.RESET + " - " + ChatColor.GOLD + "@Red_white_200#3502");
            a.set(packet, header);
            b.set(packet, footer);
            NMSBase.sendPacket(packet);
        } catch (Exception e) {
            Logger.getGeneralLogger().log(e);
        }
    }

    public static void sendDefaultScoreboardToPlayer(UHCPlayer player) {
        try {
            player.getPlayer().setScoreboard(createScoreBoard(player));
        } catch (Exception e) {
            Logger.getGeneralLogger().log(e);
        }
    }

    private static Scoreboard createScoreBoard(UHCPlayer lgp) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("UHCCoreDefault", "uhc");

        int i = 30;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC CORE");
        setScore(ChatColor.DARK_GRAY + "    ", i, objective);
        i--;

        setScore(ChatColor.AQUA + lgp.getName(), i, objective);
        i--;
        setScore(ChatColor.GRAY + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        setScore(ChatColor.GOLD + "Game number: " + ChatColor.YELLOW + lgp.getStats().getGameNumber(), i, objective);
        i--;
        setScore(ChatColor.GOLD + "Game won: " + ChatColor.YELLOW + lgp.getStats().getWinnedGames(), i, objective);
        i--;
        setScore(ChatColor.GOLD + "Win ratio: " + ChatColor.YELLOW + lgp.getStats().getPercentageOfWin(), i, objective);
        i--;
        setScore(" ", i, objective);
        i--;
        setScore(ChatColor.GOLD + "Points: " + ChatColor.YELLOW + lgp.getStats().getPoints(), i, objective);
        i--;
        setScore(ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + lgp.getStats().getKills(), i, objective);
        i--;

        setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        setScore("   ", i, objective);
        i--;
        setScore(ChatColor.DARK_GREEN + "Radi3nt", i, objective);
        return board;
    }

    private static void setScore(String text, int i, Objective objective) {
        Score score = objective.getScore(text);
        score.setScore(i);
    }

}
