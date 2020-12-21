package fr.radi3nt.uhc.uhc;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.chats.DeadChat;
import fr.radi3nt.uhc.api.chats.GameChat;
import fr.radi3nt.uhc.api.events.WinConditionsCheckEvent;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.game.UHCGameImpl;
import fr.radi3nt.uhc.api.game.instances.DefaultsParameters;
import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.lang.lang.SystemPlaceHolder;
import fr.radi3nt.uhc.api.player.*;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import fr.radi3nt.uhc.api.stats.Stats;
import fr.radi3nt.uhc.api.utilis.Config;
import fr.radi3nt.uhc.api.utilis.ScoreboardsUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

public class ClassicGame extends UHCGameImpl {

    private static final Integer GAME_POINTS = 10;
    private static final Integer DEAD_POINTS = 5;
    private static final Integer ALIVE_POINTS = 5;
    private static final Integer WINNED_BONUS = 5;
    private static final int KILLS_POINTS = 5;

    public ClassicGame() {
        super(new DefaultsParameters());
        this.data = new GameData();
        Path log = Paths.get(UHCCore.getPlugin().getDataFolder() + "/logs/game/current");
        try {
            Files.createDirectories(log);
        } catch (IOException e) {
            Logger.getGeneralLogger().log(e);
        }
        this.data.setLogChat(new Logger(Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/logs/game/current", getUUID() + ".yml"), Bukkit.getConsoleSender()));
        this.data.setDisplayName("&4&lClassic UHC");
        this.data.setDeathChat(new DeadChat());
        this.data.setGameChat(new GameChat());
        this.gameTimer = new ClassicGameTimer(this);

        ArrayList<Integer> time = new ArrayList<>();
        /*
        time.add((29 * 60 + 55) * 20);
        time.add((29 * 60 + 50) * 20);
        time.add((29 * 60 + 45) * 20);
        time.add((29 * 60 + 30) * 20);

         */
        time.add(29 * 60 * 20);
        time.add(25 * 60 * 20);
        time.add(20 * 60 * 20);
        time.add(10 * 60 * 20);
        this.PvP = new PvP(this, 30 * 60 * 20, time);
        this.data.getLogChat().log("Created game #" + getUUID());
    }

    @Override
    protected void _join(UHCPlayer player) {
        if (state == GameState.LOBBY) {
            if (!player.isPlaying()) {
                waitQueue.add(player);
                player.setWaitingGame(this);
            }
        }
    }

    @Override
    public void forceJoin(UHCPlayer player) {
        players.add(player);
        alivePlayers.add(player);
    }

    @Override
    protected void _spectate(UHCPlayer player) {
        spectators.add(player);
        player.setGameData(new PlayerGameData(this));
        player.getGameData().setPlayerState(PlayerState.SPECTATING);
    }

    @Override
    protected boolean _updateStart() {
        if (state == GameState.LOBBY) {
            if (waitQueue.size()==1) {
                Chat.broadcastMessage(UHCCore.getPrefix() + " " + ChatColor.DARK_RED + "You tried to start with only one player !");
                //return false; //todo remettre
            }

            state = GameState.WAITING;
            for (UHCPlayer lgp : waitQueue) {
                lgp.setGameData(new PlayerGameData(this));
                lgp.getGameData().setPlayerState(PlayerState.ALIVE);
                lgp.setGameInformation(new GameInformation());
                lgp.setChat(data.getGameChat());
                try {
                    lgp.sendMessage(lgp.getLanguage().getMessage("game.start.success.disconnect", lgp));
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                    Logger.getGeneralLogger().log(e);
                }

                String message = null;
                try {
                    message = lgp.getLanguage().getMessage("game.start.success.author");
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                    Logger.getGeneralLogger().log(e);
                }
                if (message != null && !message.contains(SystemPlaceHolder.getMessageReplaced("%author%", lgp))) {
                    message = message + " " + SystemPlaceHolder.getMessageReplaced("%author%", lgp);
                    lgp.sendMessage(message);
                } else {
                    lgp.sendMessage(message);
                }
                try {
                    lgp.sendTitle(data.getDisplayName(), lgp.getLanguage().getMessage("game.start.success.title", lgp), 5, 60, 5);
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                    Logger.getGeneralLogger().log(e);
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
        alivePlayers.clear();
        spectators.clear();
        players.addAll(waitQueue);
        alivePlayers.addAll(waitQueue);
        waitQueue.clear();


        parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
        parameter.getGameSpawn().getWorld().getWorldBorder().setSize(parameter.getBaseRadius() * 2);
        final int[] i = {0};
        for (UHCPlayer alivePlayer : alivePlayers) {
            /*

             */
            UHCCore.getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(UHCCore.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    UHCCore.getPlugin().getServer().getScheduler().callSyncMethod(UHCCore.getPlugin(), new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            alivePlayer.getPlayer().setInvulnerable(true);
                            alivePlayer.getPlayerStats().refresh();
                            alivePlayer.getPlayerStats().setGameMode(GameMode.ADVENTURE);
                            alivePlayer.getPlayerStats().setMaxHealth(20D);
                            alivePlayer.getPlayerStats().setHealth(20D);
                            alivePlayer.getPlayerStats().setFood(20);
                            alivePlayer.getPlayerStats().setXp(0);
                            alivePlayer.getPlayerStats().clearPotionEffect();
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
                                    player.sendMessage(player.getLanguage().getMessage("game.start.success.tp").replace("%playerName%", alivePlayer.getName()));
                                } catch (CannotFindMessageException e) {
                                    player.sendMessage(Language.NO_MESSAGE);
                                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                                    Logger.getGeneralLogger().log(e);
                                }
                                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 100, 1);
                            }
                            i[0]++;
                            return null;
                        }
                    });
                }
            }, i[0] * 20L);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                if (i[0] == alivePlayers.size()) {
                    cancel();
                    initGame();
                    for (UHCPlayer alivePlayer : alivePlayers) {
                        alivePlayer.getPlayer().setInvulnerable(false);
                    }
                }
            }

        }.runTaskTimerAsynchronously(UHCCore.getPlugin(), 1L, 1L);

    }

    @Override
    protected void _end(List<UHCPlayer> winners, boolean fast) {

        boolean noWinners = false;
        if (winners == null || winners.isEmpty()) {
            winners = new ArrayList<>();
            noWinners = true;
        }


        if (!noWinners)
            data.getLogChat().log("Endgame: " + winners.get(0).getName() + " won !");
        else
            data.getLogChat().log("Endgame: " + "no winner !");

        Map<UHCPlayer, Integer> kills = new HashMap<>();
        for (UHCPlayer lgp : players) {
            boolean winner = false;
            if (winners.contains(lgp))
                winner = true;

            Stats stats = lgp.getStats();
            stats.setGameNumber(stats.getGameNumber() + 1);
            stats.setPoints(stats.getPoints() + GAME_POINTS);

            if (!alivePlayers.contains(lgp)) {
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

            kills.put(lgp, (Integer) lgp.getGameInformation().getAttributeOrDefault("kills", new Attribute<>(0)).getObject());
            stats.setKills(stats.getKills() + (Integer) lgp.getGameInformation().getAttributeOrDefault("kills", new Attribute<>(0)).getObject());
            stats.setPoints(stats.getPoints() + (Integer) lgp.getGameInformation().getAttributeOrDefault("kills", new Attribute<>(0)).getObject() * KILLS_POINTS);

            if (!data.isPractice()) {
                data.getLogChat().log("-- Stats for " + lgp.getName() + " --");
                data.getLogChat().log("points: " + lgp.getStats().getPoints() + " | " + stats.getPoints());
                data.getLogChat().log("kills: " + lgp.getStats().getKills() + " | " + stats.getKills());
                lgp.setStats(stats);
            }

            if (getSpectatorsAndAlivePlayers().contains(lgp)) {
                if (lgp.isOnline()) {
                    lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);
                    if (winner) {
                        lgp.sendTitle("§a§lVictoire !", "§6Vous avez gagné la partie.", 5, 200, 5);
                        //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + WINNED_BONUS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(winned)");
                    } else if (noWinners) {
                        lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);
                    } else {
                        lgp.sendTitle("§c§lDéfaite...", "§4Vous avez perdu la partie.", 5, 200, 5);
                    }
                }
                //lgp.sendMessage(UHCCore.getPrefix() + ChatColor.AQUA + " +" + GAME_POINTS + ChatColor.GOLD + " ⛁ " + ChatColor.AQUA + "(played game)");

            }
        }

        for (UHCPlayer lgp : getSpectatorsAndAlivePlayers()) {
            if (lgp.isOnline()) {
                lgp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                ScoreboardsUtil.sendDefaultTabToPlayer(lgp);
            }
            if (noWinners)
                lgp.sendMessage(UHCCore.getPrefix() + " " + ChatColor.BLUE + "Nobody won the game");
            else
                lgp.sendMessage(UHCCore.getPrefix() + " " + ChatColor.BLUE + winners.get(0).getName() + " won the game");
            lgp.setGameData(new PlayerGameData(null));
            lgp.setGameInformation(new GameInformation());
            lgp.setChat(UHCCore.DEFAULT_CHAT);
        }

        Comparator<Map.Entry<UHCPlayer, Integer>> valueComparator = new Comparator<Map.Entry<UHCPlayer, Integer>>() {
            @Override
            public int compare(Map.Entry<UHCPlayer, Integer> o1, Map.Entry<UHCPlayer, Integer> o2) {
                Integer v1 = o1.getValue();
                Integer v2 = o2.getValue();
                return v1.compareTo(v2);
            }
        };
        List<Map.Entry<UHCPlayer, Integer>> listOfEntries = new ArrayList<>(kills.entrySet());
        listOfEntries.sort(valueComparator);
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
            int i = 1;
            Chat.broadcastMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                " + ChatColor.GOLD + " Killboard " + ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                ", getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
            for (Map.Entry<UHCPlayer, Integer> uhcPlayerIntegerEntry : sortedByValue.entrySet()) {
                if (uhcPlayerIntegerEntry.getValue() == 0) {
                    continue;
                }
                if (uhcPlayerIntegerEntry.getValue() > 8) {
                    Chat.broadcastMessage(getKillboardMessage(i, ChatColor.DARK_GREEN, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()), getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
                    continue;
                }
                if (uhcPlayerIntegerEntry.getValue() > 5) {
                    Chat.broadcastMessage(getKillboardMessage(i, ChatColor.GREEN, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()), getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
                    continue;
                }
                if (uhcPlayerIntegerEntry.getValue() > 3) {
                    Chat.broadcastMessage(getKillboardMessage(i, ChatColor.GOLD, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()), getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
                    continue;
                }
                Chat.broadcastMessage(getKillboardMessage(i, ChatColor.RED, uhcPlayerIntegerEntry.getKey().getName(), uhcPlayerIntegerEntry.getValue()), getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
                i++;
            }
            Chat.broadcastMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                " + "             " + "                ", getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        }

        if (fast) {
            parameter.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
            parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
            for (UHCPlayer lgp : getSpectatorsAndAlivePlayers()) {
                resetPlayer(lgp);
            }
            parameter.getGameSpawn().getWorld().getWorldBorder().setSize(parameter.getBaseRadius() * 2);
            alivePlayers.clear();
            spectators.clear();
            clearQueue();
        } else {
            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;
                    if (i == 10 * 20) {
                        parameter.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
                        parameter.getGameSpawn().getWorld().getWorldBorder().setCenter(parameter.getGameSpawn());
                        for (UHCPlayer lgp : getSpectatorsAndAlivePlayers()) {
                            resetPlayer(lgp);
                        }
                        parameter.getGameSpawn().getWorld().getWorldBorder().setSize(parameter.getBaseRadius() * 2);
                        alivePlayers.clear();
                        spectators.clear();
                        clearQueue();
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
        lgp.getPlayerStats().clearPotionEffect();
        lgp.getPlayerStats().updateLocation();
        lgp.getPlayerStats().update();
        if (lgp.isOnline()) {
            lgp.getPlayer().setWalkSpeed(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                if (ption != null)
                    lgp.getPlayerStats().removePotionEffect(ption);
            }
            lgp.getPlayer().setWalkSpeed(0.2F);
            ScoreboardsUtil.sendDefaultScoreboardToPlayer(lgp);
            ScoreboardsUtil.sendDefaultTabToPlayer(lgp);
        }
        lgp.setGameInformation(new GameInformation());
    }


    @Override
    protected void _kill(UHCPlayer player, Reason reason, Location playerloc) {

        player.getGameData().setPlayerState(PlayerState.DEAD);

        if (player.getGameInformation().getAttributeOrDefault("killer", new Attribute<>(null)).getObject() != null) {
            Chat.broadcastMessage(player.getName() + " was killed by " + ((UHCPlayer) player.getGameInformation().getAttribute("killer").getObject()).getName(), getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        } else if (reason == Reason.KILLED_BY_MOB) {
            Chat.broadcastMessage(player.getName() + " died by a mob", getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        } else if (reason == Reason.DISCONNECTED) {
            Chat.broadcastMessage(player.getName() + " died, he had an internet problem in my opinion !", getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        } else {
            Chat.broadcastMessage(player.getName() + " died", getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        }

        alivePlayers.remove(player);
        spectate(player);
        player.setChat(data.getDeathChat());
        player.getPlayerStats().refresh();
        player.getPlayerStats().setGameMode(GameMode.SPECTATOR);
        player.getPlayerStats().update();
        playerloc.getWorld().strikeLightningEffect(playerloc);
        this.data.getLogChat().log("Killed " + player.getName());
        updateWin();
    }

    public void updateWin() {
        data.getLogChat().log("Update win");
        WinConditionsCheckEvent winConditionsCheckEvent = new WinConditionsCheckEvent(this);
        Bukkit.getPluginManager().callEvent(winConditionsCheckEvent);

        if (winConditionsCheckEvent.isCancelled())
            end(winConditionsCheckEvent.getVictoryTeam(), false);
    }

    private String getKillboardMessage(Integer place, ChatColor color, String name, Integer kills) {
        return "  " + ChatColor.BOLD + ChatColor.DARK_GRAY + place + ". " + color + ChatColor.BOLD + "" + kills + ChatColor.GRAY + " - " + name;
    }

    @Override
    public Set<UHCPlayer> getSpectatorsAndAlivePlayers() {
        Set<UHCPlayer> uhcPlayers = new HashSet<>(alivePlayers);
        uhcPlayers.addAll(spectators);
        return uhcPlayers;
    }
}
