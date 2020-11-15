package fr.radi3nt.uhc.uhc;

import fr.radi3nt.uhc.api.chats.DeadChat;
import fr.radi3nt.uhc.api.chats.GameChat;
import fr.radi3nt.uhc.api.events.WinConditionsCheckEvent;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.game.UHCGameImpl;
import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.lang.lang.SystemPlaceHolder;
import fr.radi3nt.uhc.api.player.NullPlayerGameData;
import fr.radi3nt.uhc.api.player.PlayerGameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import fr.radi3nt.uhc.api.stats.Stats;
import fr.radi3nt.uhc.api.utilis.Config;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class ClassicGame extends UHCGameImpl {

    private static final Integer GAME_POINTS = 10;
    private static final Integer DEAD_POINTS = 5;
    private static final Integer ALIVE_POINTS = 5;
    private static final Integer WINNED_BONUS = 5;
    private static final int KILLS_POINTS = 5;

    public ClassicGame() {
        this.data = new GameData();
        new File(UHCCore.getPlugin().getDataFolder() + "/logs/game", "latest.yml").delete();
        this.data.setLogChat(new Logger(Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/logs/game", "latest.yml"), Bukkit.getConsoleSender()));
        this.data.setDisplayName("&4&lClassic UHC");
        this.data.setDeathChat(new DeadChat());
        this.data.setGameChat(new GameChat());
        this.gameTimer = new ClassicGameTimer(this);

        ArrayList<Integer> time = new ArrayList<>();
        time.add((29 * 60 + 55) * 20);
        time.add((29 * 60 + 50) * 20);
        time.add((29 * 60 + 45) * 20);
        time.add((29 * 60 + 30) * 20);
        time.add(29 * 60 * 20);
        time.add(25 * 60 * 20);
        time.add(20 * 60 * 20);
        time.add(10 * 60 * 20);
        this.PvP = new PvP(this, 30 * 60 * 20, time);
    }

    @Override
    protected void _join(UHCPlayer player) {
        if (state == GameState.LOBBY) {
            alivePlayers.add(player);
            deadPlayers.add(player);
        }
    }

    @Override
    protected boolean _updateStart() {
        if (state == GameState.LOBBY) {
            state = GameState.WAITING;
            for (UHCPlayer lgp : alivePlayers) {
                lgp.setGameData(new ClassicPlayerGameData(this));
                lgp.setChat(data.getGameChat());
                try {
                    lgp.sendMessage(lgp.getLanguage().getMessage("game.start.success.disconnect", lgp));
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                }

                String message = null;
                try {
                    message = lgp.getLanguage().getMessage("game.start.success.author");
                } catch (CannotFindMessageException e) {

                }
                if (message != null && !message.contains("%author%")) {
                    message = message + " " + SystemPlaceHolder.getMessageReplaced("%author%", lgp);
                    lgp.sendMessage(message);
                } else {
                    try {
                        lgp.sendMessage(lgp.getLanguage().getMessage("game.start.success.author", lgp));
                    } catch (CannotFindMessageException e) {
                        lgp.sendMessage(Language.NO_MESSAGE);
                        Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                    }
                }
                try {
                    lgp.sendTitle(data.getDisplayName(), lgp.getLanguage().getMessage("game.start.success.title", lgp), 5, 60, 5);
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    start();
                }
            }.runTaskLater(UHCCore.getPlugin(), 2 * 20);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void _start() {

        parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
        for (UHCPlayer alivePlayer : alivePlayers) {
            alivePlayer.getPlayer().setInvulnerable(true);
            alivePlayer.getPlayerStats().refresh();
            alivePlayer.getPlayerStats().setGameMode(GameMode.ADVENTURE);
            alivePlayer.getPlayerStats().setMaxHealth(20D);
            alivePlayer.getPlayerStats().setHealth(20D);
            alivePlayer.getPlayerStats().setFood(20);
            alivePlayer.getPlayerStats().setXp(0);
            alivePlayer.getPlayerStats().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 180, true, false));
            alivePlayer.getPlayerStats().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 8, true, false));
            alivePlayer.getPlayerStats().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 0, true, false));
            alivePlayer.getPlayerStats().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 5, true, false));
            alivePlayer.getPlayerStats().getInventory().clear();

            alivePlayer.getPlayerStats().update();
            alivePlayer.getPlayer().setSaturation(20f);
            alivePlayer.getPlayer().setWalkSpeed(0);
            alivePlayer.getPlayer().setFireTicks(0);
            alivePlayer.getPlayer().setExp(0);
            alivePlayer.getPlayer().setLevel(0);
            alivePlayer.getPlayer().setTotalExperience(0);

            scatter(alivePlayer);

            for (UHCPlayer player : alivePlayers) {
                try {
                    player.sendMessage(player.getLanguage().getMessage("game.start.success.tp").replace("%playerName%", player.getName()));
                } catch (CannotFindMessageException e) {
                    player.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                }
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 100, 1);
            }

            alivePlayer.getPlayer().setInvulnerable(false);
        }

    }

    @Override
    protected void _end(List<UHCPlayer> winners, boolean fast) {

        boolean noWinners = false;
        if (winners == null || winners.isEmpty()) {
            winners = new ArrayList<>();
            noWinners = true;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(onlinePlayer);
            lgp.setChat(UHCCore.DEFAULT_CHAT);
        }


        if (!noWinners)
            data.getLogChat().log("Endgame: " + winners.get(0).getName() + " won !");
        else
            data.getLogChat().log("Endgame: " + "no winner !");

        for (UHCPlayer spectator : spectators) {
            spectator.getPlayerStats().refresh();
            spectator.getPlayerStats().setGameMode(Bukkit.getDefaultGameMode());
            if (spectator.isOnline()) {
                spectator.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            spectator.getPlayerStats().setLastLocation(parameter.getSpawn());
            spectator.getPlayerStats().updateLocation();
            spectator.getPlayerStats().update();
        }
        spectators.clear();

        Map<UHCPlayer, Integer> kills = new HashMap<>();
        for (UHCPlayer lgp : getDeadAndAlivePlayers()) {
            boolean winner = false;
            if (noWinners)
                lgp.sendMessage(UHCCore.getPrefix() + " " + ChatColor.BLUE + "Nobody won the game");
            else
                lgp.sendMessage(UHCCore.getPrefix() + " " + ChatColor.BLUE + winners.get(0).getName() + " won the game");

            if (winners.contains(lgp))
                winner = true;

            Stats stats = lgp.getStats();
            data.getLogChat().log("-- Initial Stats for " + lgp.getName() + " --");
            data.getLogChat().log("points: " + stats.getPoints());
            data.getLogChat().log("games: " + stats.getGameNumber());
            data.getLogChat().log("won: " + stats.getWinnedGames());
            data.getLogChat().log("kills: " + stats.getKills());
            stats.setGameNumber(stats.getGameNumber() + 1);
            stats.setPoints(stats.getPoints() + GAME_POINTS);
            //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + GAME_POINTS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(played game)");

            if (lgp.getGameData().isDead()) {
                stats.setPoints(stats.getPoints() + DEAD_POINTS);
                //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + DEAD_POINTS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(dead)");
            } else {
                stats.setPoints(stats.getPoints() + ALIVE_POINTS);
                //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + ALIVE_POINTS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(alive)");
            }

            if (winner) {
                stats.setWinnedGames(stats.getWinnedGames() + 1);
                stats.setPoints(stats.getPoints() + WINNED_BONUS);
            }

            if (lgp.getPlayer() != null) {
                lgp.getPlayer().closeInventory();
                lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);

                if (winner) {
                    lgp.sendTitle("§a§lVictoire !", "§6Vous avez gagné la partie.", 5, 200, 5);
                    //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + WINNED_BONUS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(winned)");
                } else if (noWinners) {
                    lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);
                } else {
                    lgp.sendTitle("§c§lDéfaite...", "§4Vous avez perdu la partie.", 5, 200, 5);
                }


                lgp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            kills.put(lgp, lgp.getGameData().getKills());
            stats.setKills(stats.getKills() + lgp.getGameData().getKills());
            stats.setPoints(stats.getPoints() + lgp.getGameData().getKills() * KILLS_POINTS);

            if (!data.isPractice()) {
                lgp.setStats(stats);
            }
            lgp.setGameData(new NullPlayerGameData());

            Comparator<Map.Entry<UHCPlayer, Integer>> valueComparator = new Comparator<Map.Entry<UHCPlayer, Integer>>() {
                @Override
                public int compare(Map.Entry<UHCPlayer, Integer> o1, Map.Entry<UHCPlayer, Integer> o2) {
                    Integer v1 = o1.getValue();
                    Integer v2 = o2.getValue();
                    return v1.compareTo(v2);
                }
            };
            List<Map.Entry<UHCPlayer, Integer>> listOfEntries = new ArrayList<>(kills.entrySet());
            Collections.sort(listOfEntries, valueComparator);
            LinkedHashMap<UHCPlayer, Integer> sortedByValue = new LinkedHashMap<>(listOfEntries.size());
            for (Map.Entry<UHCPlayer, Integer> entry : listOfEntries) {
                sortedByValue.put(entry.getKey(), entry.getValue());
            }

            boolean hasKill = false;
            for (Map.Entry<UHCPlayer, Integer> uhcPlayerIntegerEntry : sortedByValue.entrySet()) {
                if (uhcPlayerIntegerEntry.getValue() != 0) {
                    hasKill = true;
                    break;
                }
            }

            if (hasKill) {
                lgp.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                " + ChatColor.GOLD + " Killboard " + ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                ");
                for (Map.Entry<UHCPlayer, Integer> uhcPlayerIntegerEntry : sortedByValue.entrySet()) {
                    if (uhcPlayerIntegerEntry.getValue() == 0) {
                        continue;
                    }
                    if (uhcPlayerIntegerEntry.getValue() > 8) {
                        lgp.sendMessage(getKillboardMessage(ChatColor.DARK_GREEN, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()));
                        continue;
                    }
                    if (uhcPlayerIntegerEntry.getValue() > 5) {
                        lgp.sendMessage(getKillboardMessage(ChatColor.GREEN, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()));
                        continue;
                    }
                    if (uhcPlayerIntegerEntry.getValue() > 3) {
                        lgp.sendMessage(getKillboardMessage(ChatColor.GOLD, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()));
                        continue;
                    }
                    if (uhcPlayerIntegerEntry.getValue() >= 3) {
                        lgp.sendMessage(getKillboardMessage(ChatColor.RED, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()));
                        continue;
                    }
                }
                lgp.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                " + "            " + "                ");
            }


            data.getLogChat().log("-- Modified Stats for " + lgp.getName() + " --");
            data.getLogChat().log("points: " + stats.getPoints());
            data.getLogChat().log("games: " + stats.getGameNumber());
            data.getLogChat().log("won: " + stats.getWinnedGames());
            data.getLogChat().log("kills: " + stats.getKills());
        }
        if (fast) {
            parameter.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
            parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
            for (UHCPlayer lgp : getDeadAndAlivePlayers()) {
                resetPlayer(lgp);
            }
            parameter.getGameSpawn().getWorld().getWorldBorder().setSize(parameter.getBaseRadius() * 2);
            getAlivePlayers().clear();
            getDeadPlayers().clear();
        } else {
            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;
                    if (i == 10 * 20) {
                        parameter.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
                        parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
                        for (UHCPlayer lgp : getDeadAndAlivePlayers()) {
                            resetPlayer(lgp);
                        }
                        parameter.getGameSpawn().getWorld().getWorldBorder().setSize(parameter.getBaseRadius() * 2);
                        getAlivePlayers().clear();
                        getDeadPlayers().clear();
                    }
                }
            }.runTaskTimer(UHCCore.getPlugin(), 1, 1);
        }
    }


    private void resetPlayer(UHCPlayer lgp) {
        lgp.getPlayerStats().refresh();
        lgp.getPlayerStats().getInventory().clear();
        lgp.getPlayerStats().setXp(0);
        lgp.getPlayerStats().setFood(20);
        lgp.getPlayerStats().setMaxHealth(20);
        lgp.getPlayerStats().setHealth(20);
        lgp.getPlayerStats().setGameMode(Bukkit.getServer().getDefaultGameMode());
        lgp.getPlayerStats().setLastLocation(parameter.getSpawn());
        lgp.getPlayerStats().updateLocation();
        lgp.getPlayerStats().update();
        if (lgp.isOnline()) {
            lgp.getPlayer().setWalkSpeed(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                if (ption != null)
                    lgp.getPlayerStats().removePotionEffect(ption);
            }
            lgp.getPlayer().setWalkSpeed(0.2F);
        }
        lgp.getGameData().setKills(0);
        lgp.getGameData().setKiller(null);
        lgp.setGameData(new NullPlayerGameData());
    }

    @Override
    protected void _kill(UHCPlayer player, Reason reason, Location playerloc) {
        alivePlayers.remove(player);
        player.getPlayerStats().refresh();
        player.setChat(data.getDeathChat());
        player.getPlayerStats().setGameMode(GameMode.SPECTATOR);
        playerloc.getWorld().strikeLightningEffect(playerloc);

        PlayerGameData data = new ClassicPlayerGameData(null);
        data.setDead(true);
        data.setKills(player.getGameData().getKills());
        data.setKiller(player.getGameData().getKiller());
        player.setGameData(data);

        if (data.getKiller() != null) {
            UHCCore.broadcastMessage(player.getName() + " was killed by " + data.getKiller().getName());
        } else {
            UHCCore.broadcastMessage(player.getName() + " died");
        }
        updateKill(false);
        this.data.getLogChat().log("Killed " + player.getName());
    }

    private void updateKill(boolean endGame) {
        data.getLogChat().log("Update kill / endgame: " + endGame);
        if (endGame) {
            this.end(new ArrayList<>(), false);
            return;
        }
        WinConditionsCheckEvent winConditionsCheckEvent = new WinConditionsCheckEvent(this);
        Bukkit.getPluginManager().callEvent(winConditionsCheckEvent);

        if (winConditionsCheckEvent.isCancelled())
            end(winConditionsCheckEvent.getVictoryTeam(), false);
    }

    private String getKillboardMessage(ChatColor color, String name, Integer kills) {
        return "  " + color + "" + kills + " " + ChatColor.GRAY + name;
    }


    @Override
    public Set<UHCPlayer> getDeadAndAlivePlayers() {
        Set<UHCPlayer> set = new HashSet<>();
        set.addAll(alivePlayers);
        set.addAll(deadPlayers);
        return set;
    }

    @Override
    public Location getGameSpawn() {
        return parameter.getGameSpawn();
    }
}
