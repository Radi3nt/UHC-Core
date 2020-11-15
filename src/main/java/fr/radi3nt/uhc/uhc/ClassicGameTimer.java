package fr.radi3nt.uhc.uhc;

import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.player.npc.NMSBase;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import org.bukkit.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;

public class ClassicGameTimer extends GameTimer {

    private final int shift = 1000;
    private boolean waiting = true;

    public ClassicGameTimer(UHCGame game) {
        super(game);
    }

    @Override
    public void _run() {
        try {
            if (waiting) {
                game.getGameSpawn().getWorld().setTime(24000 - shift);
                game.getGameSpawn().getWorld().setFullTime(24000 - shift);
                for (UHCPlayer gamePlayer : game.getDeadAndAlivePlayers()) {
                    if (gamePlayer.isOnline()) {
                        if (ticks == 1) {
                            gamePlayer.sendTitle(ChatColor.GOLD + "3", "", 10, 20, 10);
                            gamePlayer.sendMessage(ChatColor.DARK_GRAY + "Starting in " + ChatColor.DARK_GREEN + ChatColor.BOLD + "3" + ChatColor.DARK_GRAY + " seconds");
                            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1.2f);
                        }
                        if (ticks == 20) {
                            gamePlayer.sendTitle(ChatColor.GOLD + "2", "", 10, 20, 10);
                            gamePlayer.sendMessage(ChatColor.DARK_GRAY + "Starting in " + ChatColor.GOLD + ChatColor.BOLD + "2" + ChatColor.DARK_GRAY + " seconds");
                            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1.1f);
                        }
                        if (ticks == 2 * 20) {
                            gamePlayer.sendTitle(ChatColor.GOLD + "1", "", 10, 20, 10);
                            gamePlayer.sendMessage(ChatColor.DARK_GRAY + "Starting in " + ChatColor.DARK_RED + ChatColor.BOLD + "1" + ChatColor.DARK_GRAY + " seconds");
                            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1f);
                        }
                    }
                    if (ticks == 3 * 20) {
                        try {
                            gamePlayer.sendTitle(ChatColor.GOLD + gamePlayer.getLanguage().getMessage("game.timer.start.title", gamePlayer), gamePlayer.getLanguage().getMessage("game.timer.start.subtitle", gamePlayer), 20, 20 * 3, 20);
                        } catch (CannotFindMessageException e) {
                            gamePlayer.sendMessage(Language.NO_MESSAGE);
                            Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                        }
                        if (gamePlayer.isOnline()) {
                            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, SoundCategory.AMBIENT, 1f, 1f);
                            gamePlayer.getPlayer().setWalkSpeed(0.2F);
                        }
                        gamePlayer.getPlayerStats().refresh();
                        gamePlayer.getPlayerStats().setGameMode(GameMode.SURVIVAL);
                        gamePlayer.getPlayerStats().removePotionEffect(PotionEffectType.JUMP);
                        gamePlayer.getPlayerStats().removePotionEffect(PotionEffectType.SLOW);
                        gamePlayer.getPlayerStats().removePotionEffect(PotionEffectType.BLINDNESS);
                        gamePlayer.getPlayerStats().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        gamePlayer.getPlayerStats().update();
                        this.ticks = 0;
                        waiting = false;
                    }
                }
            }
            for (UHCPlayer spectator : game.getSpectators()) {
                if (ticks % 20 == 0) {
                    if (spectator.isOnline()) {
                        spectator.getPlayer().setScoreboard(createSpectatorScoreBoard(spectator));
                    }
                }
            }

            for (UHCPlayer spectator : game.getDeadPlayers()) {
                if (ticks % 20 == 0) {
                    if (spectator.isOnline()) {
                        spectator.getPlayer().setScoreboard(createSpectatorScoreBoard(spectator));
                    }
                }
            }

            for (UHCPlayer lgp : game.getAlivePlayers()) {
                lgp.getPlayerStats().refresh();

                if (lgp.getPlayer() != null) {
                    lgp.getPlayer().setCompassTarget(game.getGameSpawn());


                    if (ticks % 20 == 0) {
                        lgp.getPlayer().setScoreboard(createScoreBoard(lgp));
                        try {
                            Object packet = NMSBase.getNMSClass("PacketPlayOutPlayerListHeaderFooter").newInstance();
                            Field a = packet.getClass().getDeclaredField("a");
                            a.setAccessible(true);
                            Field b = packet.getClass().getDeclaredField("b");
                            b.setAccessible(true);
                            Object header = new ChatComponentText(game.getData().getDisplayName() + "\n");//todo custom text
                            Object footer = new ChatComponentText(ChatColor.GOLD + "Dev by " + ChatColor.AQUA + "" + ChatColor.BOLD + "Radi3nt");
                            a.set(packet, header);
                            b.set(packet, footer);
                            NMSBase.sendPacket(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getGeneralLogger().log(e);
        } finally {
            ticks++;
        }
    }

    private Scoreboard createScoreBoard(UHCPlayer lgp) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("LG UHC", "uhc");

        int i = 30;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', game.getData().getDisplayName()));
        setScore(ChatColor.DARK_GRAY + "    ", i, objective);
        i--;

        setScore(ChatColor.RED + "" + game.getAlivePlayers().size() + ChatColor.DARK_RED + " joueurs", i, objective);
        i--;

        setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        int heures = (getTicks() / 20 / 3600);
        int minutes = ((getTicks() / 20 - (getTicks() / 20 / 3600) * 3600) / 60);
        int seconds = getTicks() / 20 - (heures * 3600 + minutes * 60);
        setScore(ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d", heures) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), i, objective);
        i--;

        if (game.getPvP().isTimerActivated() && !game.getPvP().isPvp()) {
            int ticksPvp = game.getPvP().getTime() - getTicks();
            int heures1 = (ticksPvp / 20 / 3600);
            int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
            int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);

            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.YELLOW + String.format("%02d", heures1) + ":" + String.format("%02d", minutes1) + ":" + String.format("%02d", seconds1), i, objective);
        } else if (game.getPvP().isPvp()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.GREEN + "Activated", i, objective);
        } else if (!game.getPvP().isTimerActivated()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.DARK_RED + "Deactivated", i, objective);
        }
        i--;

        setScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective); //todo tro longue barres
        i--;


        setScore(ChatColor.DARK_BLUE + "Kills: " + ChatColor.BLUE + lgp.getGameData().getKills(), i, objective);
        i--;

        setScore(ChatColor.AQUA + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        String text;
        if (game.getRadius() == (game.getParameters().getBaseRadius()))
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + ChatColor.UNDERLINE + "+" + ChatColor.GREEN + game.getRadius();
        else
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + game.getRadius();

        setScore(text, i, objective);
        i--;

        setScore("   ", i, objective);
        i--;
        setScore(ChatColor.DARK_GREEN + "@Radi3nt", i, objective);
        return board;
    }

    private Scoreboard createSpectatorScoreBoard(UHCPlayer lgp) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("LG UHC", "uhc");

        int i = 30;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', game.getData().getDisplayName()));
        setScore(ChatColor.DARK_GRAY + "    ", i, objective);
        i--;

        setScore(ChatColor.RED + "" + game.getAlivePlayers().size() + ChatColor.DARK_RED + " joueurs", i, objective);
        i--;

        setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        int heures = (getTicks() / 20 / 3600);
        int minutes = ((getTicks() / 20 - (getTicks() / 20 / 3600) * 3600) / 60);
        int seconds = getTicks() / 20 - (heures * 3600 + minutes * 60);

        setScore(ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d", heures) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), i, objective);
        i--;

        if (game.getPvP().isTimerActivated() && !game.getPvP().isPvp()) {
            int ticksPvp = game.getPvP().getTime() - getTicks();
            int heures1 = (ticksPvp / 20 / 3600);
            int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
            int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);

            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.YELLOW + String.format("%02d", heures1) + ":" + String.format("%02d", minutes1) + ":" + String.format("%02d", seconds1), i, objective);
        } else if (game.getPvP().isPvp()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.GREEN + "Activated", i, objective);
        } else if (!game.getPvP().isTimerActivated()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.DARK_RED + "Deactivated", i, objective);
        }
        i--;

        setScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective); //todo tro longue barres
        i--;

        String text;
        if (game.getRadius() == (game.getParameters().getBaseRadius()))
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + ChatColor.UNDERLINE + "+" + ChatColor.GREEN + game.getRadius();
        else
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + game.getRadius();

        setScore(text, i, objective);
        i--;

        setScore("   ", i, objective);
        i--;
        setScore(ChatColor.DARK_GREEN + "by Radi3nt", i, objective);
        return board;
    }

    private void setScore(String text, int i, Objective objective) {
        Score score = objective.getScore(text);
        score.setScore(i);
    }

    @Override
    public int getTicks() {
        if (!waiting)
            return super.getTicks();
        return 0;
    }
}
