/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.uhc.uhc;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.chats.GeneralChat;
import fr.radi3nt.uhc.api.command.UHCCommands;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameType;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.game.instances.DefaultsParameters;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.Reader;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.listeners.*;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.scenario.*;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVote;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtils;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.api.utilis.Config;
import fr.radi3nt.uhc.api.utilis.Updater;
import fr.radi3nt.uhc.uhc.listeners.ClassicGameCheck;
import fr.radi3nt.uhc.uhc.listeners.DefaultPlayerKilledFinisher;
import fr.radi3nt.uhc.uhc.listeners.UHCPlayerKillAnotherListener;
import fr.radi3nt.uhc.uhc.listeners.gui.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

public final class UHCCore extends JavaPlugin implements Listener {

    public static final Chat DEFAULT_CHAT = new GeneralChat();
    public static final String DEFAULT_LANG_ID = "fr";
    private static final SecureRandom random = new SecureRandom();

    private static final Set<UHCPlayer> players = new HashSet<>();
    private static final List<UHCGame> gameQueue = new ArrayList<>();
    private static final Set<GameType> registeredGames = new HashSet<>();
    private static final String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + ChatColor.BOLD + "UHC" + ChatColor.AQUA + "]" + ChatColor.RESET;
    private static Plugin plugin;
    private static Updater updater;

    public static void registerScenario(Class<? extends Scenario> scenario) {
        ScenarioUtils.addScenario(scenario);
    }

    public static void registerGame(GameType gameType) {
        if (gameType.getID().contains(" "))
            return;
        for (GameType registeredGame : registeredGames) {
            if (registeredGame.getID().equalsIgnoreCase(gameType.getID()))
                return;
            if (registeredGame.getGameClass().equals(gameType.getGameClass()))
                return;
        }
        registeredGames.add(gameType);
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
        Logger.getChat().log(message);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Set<UHCPlayer> getPlayers() {
        return players;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static Updater getUpdater() {
        return updater;
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public static List<UHCGame> getGameQueue() {
        return gameQueue;
    }

    private void RegisterCommands() {
        UHCCommands commands = new UHCCommands();
        getCommand("uhc").setExecutor(commands);
        getCommand("uhc").setTabCompleter(commands);

    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new SmallFeaturesListener(), this);


        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDie(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerPreProcessCommand(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);


        getServer().getPluginManager().registerEvents(new ClassicGameCheck(), this);
        getServer().getPluginManager().registerEvents(new DefaultPlayerKilledFinisher(), this);
        getServer().getPluginManager().registerEvents(new UHCPlayerKillAnotherListener(), this);
    }

    @Override
    public void onEnable() {

        plugin = UHCCore.getPlugin(UHCCore.class);
        Reader reader = new Reader();
        try {

            Path logGeneral = Paths.get(getDataFolder() + "/logs/general");
            Path logChat = Paths.get(getDataFolder() + "/logs/chat");
            Path logGame = Paths.get(getDataFolder() + "/logs/game");
            Path logGeneralLatest = Paths.get(logGeneral.toString() + "/latest.yml");
            Path logChatLatest = Paths.get(logChat.toString() + "/latest.yml");
            Files.createDirectories(logGeneral);
            Files.createDirectories(logChat);
            Files.createDirectories(logGame);
            try {
                Files.delete(logGeneralLatest);
                Files.delete(logChatLatest);
            } catch (Exception ignored) {
            }

            try {
                Files.createFile(logGeneralLatest);
                Files.createFile(logChatLatest);
            } catch (FileAlreadyExistsException ignored) {
            }


            Logger.setGeneralLogger(new Logger(Config.createConfig(getDataFolder() + "/logs/general", "latest.yml"), Bukkit.getConsoleSender()));
            Logger.setChat(new Logger(Config.createConfig(getDataFolder() + "/logs/chat", "latest.yml"), Bukkit.getConsoleSender()));

            try {
                Path path = Paths.get(getDataFolder() + "/langs");
                Files.createDirectories(path);
                Path path3 = Paths.get(getDataFolder() + "/players");
                Files.createDirectories(path3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.YELLOW + "Starting up !");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.YELLOW + "Loup Garou UHC plugin (version " + plugin.getDescription().getVersion() + ") by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.YELLOW + "If you have any issues, please report it");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.AQUA + "--------------------------------------");


            Logger.getGeneralLogger().log("Loading default language");
            new Language(null, "Default", Language.DEFAULTID);
            reader.copyLocalToLang();

            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Registered PlaceHolders");
            RegisterEvents();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Registered Events");
            try {
                registerDefaultScenarios();
                Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Loading Default Scenarios");
            } catch (Exception e) {
                Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.DARK_RED + "Couldn't load default scenarios, some scenarios could be missing");
            }
            RegisterCommands();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Registered Commands");


            HoloStats.createHoloStatsScoreboards();


            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.GREEN + "Finished initialisation");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.AQUA + "--------------------------------------");
        } catch (Exception e) {
            try {
                Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.DARK_RED + "Something went wrong, please restart your server");
                Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.AQUA + "--------------------------------------");
                e.printStackTrace();
                Logger.getGeneralLogger().log(e);
                Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.AQUA + "--------------------------------------");
            } catch (Exception e1) {
                System.out.println("[UHC] Cannot start plugin due to an internal error:");
                e.printStackTrace();
            } finally {
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            }
            return;
        }
        registerAllLanguages(reader);

        gameQueue.add(new ClassicGame());
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                loadPlayersOnline();
                UHCCore.registerGame(new GameType(ClassicGame.class, DefaultsParameters.class, "Classic UHC", "ClassicUHC", "ClassicUHC"));
            }
        }, 0);
    }

    private void loadPlayersOnline() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(player);
            lgp.loadStats();
            lgp.loadSavedLang();
            players.add(lgp);
            lgp.setChat(DEFAULT_CHAT);
            broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        }
    }

    private void registerAllLanguages(Reader reader) {
        Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Starting languages indexing");
        if (!reader.loadAllLanguage())
            Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Error while loading languages configs");
        Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[UHC] " + ChatColor.RED + "Registered Languages");
    }

    private void registerDefaultScenarios() {

        ScenarioUtils.addScenario(PlayerVote.class);
        ScenarioUtils.addScenario(XPBoost.class);
        ScenarioUtils.addScenario(VanillaPlus.class);
        ScenarioUtils.addScenario(TimedCommands.class);
        ScenarioUtils.addScenario(Timber.class);
        ScenarioUtils.addScenario(StarterItems.class);
        ScenarioUtils.addScenario(OreLimiter.class);
        ScenarioUtils.addScenario(NoFall.class);
        ScenarioUtils.addScenario(NoClean.class);
        ScenarioUtils.addScenario(FireLess.class);
        ScenarioUtils.addScenario(FinalHeal.class);
        ScenarioUtils.addScenario(CutClean.class);
        ScenarioUtils.addScenario(AutoBreak.class);
        ScenarioUtils.addScenario(DeadlyWater.class);
        ScenarioUtils.addScenario(FastFurnace.class);
        ScenarioUtils.addScenario(HastyBoys.class);
        ScenarioUtils.addScenario(LimitedEnchants.class);
        ScenarioUtils.addScenario(NoPoison.class);
        ScenarioUtils.addScenario(RodLess.class);
        ScenarioUtils.addScenario(DoubleJump.class);
        ScenarioUtils.addScenario(HungerLess.class);
        ScenarioUtils.addScenario(NoDrown.class);
        ScenarioUtils.addScenario(FastPlace.class);
        ScenarioUtils.addScenario(NoAbsorbtion.class);
        ScenarioUtils.addScenario(HorseLess.class);
        ScenarioUtils.addScenario(FightDetector.class);
        ScenarioUtils.addScenario(UHC.class);
        ScenarioUtils.addScenario(WorldBorder.class);
        ScenarioUtils.addScenario(StartInvincibility.class);
        ScenarioUtils.addScenario(CenterDistance.class);
        //ScenarioUtils.addScenario(Meetup.class);
        ScenarioUtils.addScenario(MeetupAlert.class);

    }

    @Override
    public void onDisable() {
        try {
            Logger.getGeneralLogger().archive();
            Logger.getChat().archive();
        } catch (Exception e) {
            System.out.println("[UHC] Cannot archive logger !");
        }

        for (int i1 = 0; i1 < UHCCore.getGameQueue().size(); i1++) {
            UHCGame game = UHCCore.getGameQueue().get(i1);
            game.end(null, true);
        }


        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(onlinePlayer);
            lgp.saveLang();
            lgp.saveStats();
        }


        for (int i = 0; i < HoloStats.getCachedHolo().size(); i++) {
            HoloStats holoStats = HoloStats.getCachedHolo().get(i);
            holoStats.remove();
            i--;
        }
        HoloStats.getCachedHolo().clear();
    }

    public static void handleCannotFindMessageException(CannotFindMessageException e, UHCPlayer player) {
        player.sendMessage(Language.NO_MESSAGE);
        handleCannotFindMessageException(e);
    }

    public static void handleCannotFindMessageException(CannotFindMessageException e) {
        Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
    }

    public static Set<GameType> getRegisteredGames() {
        return registeredGames;
    }

    public static SecureRandom getRandom() {
        return random;
    }
}