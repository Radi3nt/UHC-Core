/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.chats.DeadChat;
import fr.radi3nt.uhc.api.chats.GameChat;
import fr.radi3nt.uhc.api.chats.GeneralChat;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.lang.Reader;
import fr.radi3nt.uhc.api.lang.lang.Languages;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.loupgarouuhc.classes.param.Parameters;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.commands.LGTabCompleter;
import fr.radi3nt.loupgarouuhc.commands.LGcommands;
import fr.radi3nt.loupgarouuhc.event.customlisteners.*;
import fr.radi3nt.loupgarouuhc.event.listeners.*;
import fr.radi3nt.loupgarouuhc.event.listeners.gui.ClickEvent;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou.*;
import fr.radi3nt.loupgarouuhc.roles.roles.Solo.Assassin;
import fr.radi3nt.loupgarouuhc.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.roles.roles.Villagers.*;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioCommands;
import fr.radi3nt.uhc.api.scenarios.scenario.*;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVote;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtilis;
import fr.radi3nt.uhc.api.utilis.Config;
import fr.radi3nt.uhc.api.utilis.Updater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class LoupGarouUHC extends JavaPlugin implements Listener {

    public static Parameters parameters;

    public static Chat GeneralChatI;
    public static Chat GameChatI;
    public static Chat DeadChatI;

    private static final HashMap<String, Integer> roleNumber = new HashMap<>();
    private static final Set<UHCPlayer> players = new HashSet<>();
    private static final String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + ChatColor.BOLD + "LG UHC" + ChatColor.AQUA + "]" + ChatColor.RESET;
    private static final String prefixPrivé = ChatColor.BLUE + "[Privé]" + ChatColor.RESET;
    private static Plugin plugin;
    private static FileConfiguration RolesConfig;
    private static Updater updater;
    public static final String DEFAULT_LANG_ID = "fr";
    private static File RoleConfigFile;
    private static LGGame GameInstance;
    private final HashMap<RoleIdentity, Boolean> MaxRoles = new HashMap<>();

    public static void registerScenario(Class<? extends Scenario> scenario) {
        ScenarioUtilis.addScenario(scenario);
    }

    public static void registerRole(Class<? extends Role> role) {
        try {
            registerRole((RoleIdentity) role.getMethod("getStaticRoleIdentity").invoke(null), role);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Logger.getGeneralLogger().log("Cannot register role: " + role.getSimpleName());
            Logger.getGeneralLogger().log(e);
        }
    }

    private void registerConfigs() {
        File locations = new File(getDataFolder(), "roles.yml");
        if (!locations.exists()) {
            try {
                locations.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RolesConfig = YamlConfiguration.loadConfiguration(locations);
        RoleConfigFile = locations;
    }


    private void RegisterCommands() {
        getCommand("lg").setExecutor(new LGcommands());
        getCommand("lg").setTabCompleter(new LGTabCompleter());
        getCommand("uhc").setExecutor(new ScenarioCommands());
    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(), this);


        getServer().getPluginManager().registerEvents(new OnPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDie(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerPreProcessCommand(), this);

        getServer().getPluginManager().registerEvents(new ClickEvent(), this);

        getServer().getPluginManager().registerEvents(new OnDayEvent(), this);
        getServer().getPluginManager().registerEvents(new OnNightEvent(), this);
        getServer().getPluginManager().registerEvents(new OnNewEpisodeEvent(), this);
        getServer().getPluginManager().registerEvents(new OnDiscoverRoleEvent(), this);
        getServer().getPluginManager().registerEvents(new OnKilledEvent(), this);
        getServer().getPluginManager().registerEvents(new OnKillEvent(), this);
        getServer().getPluginManager().registerEvents(new OnStartGame(), this);
        getServer().getPluginManager().registerEvents(new OnEndGame(), this);
        getServer().getPluginManager().registerEvents(new OnWinCheck(), this);


        getServer().getPluginManager().registerEvents(new SmallFeaturesListener(), this);
    }

    public static void registerRole(RoleIdentity identity, Class<? extends Role> role) {
        try {
            Role.getRoleLinkByStringKey().put(identity, role.getConstructor(LGGame.class));
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }

    public static FileConfiguration getRolesConfig() {
        return RolesConfig;
    }

    public static void saveRoleFile() {
        try {
            RolesConfig.save(RoleConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadGameInstance() {
        GameInstance = new LGGame(parameters);
    }


    public static Parameters getParameters() {
        return parameters;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static HashMap<String, Integer> getRoleNumber() {
        return roleNumber;
    }

    public static Set<UHCPlayer> getPlayers() {
        return players;
    }

    public static LGGame getGameInstance() {
        return GameInstance;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getPrefixPrivé() {
        return prefixPrivé;
    }

    public static Updater getUpdater() {
        return updater;
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
        Logger.getChat().log(message);
    }

    @Override
    public void onEnable() {

        try {

            new File(getDataFolder() + "/logs/general", "latest.yml").delete();
            new File(getDataFolder() + "/logs/chat", "latest.yml").delete();

            Logger.setGeneralLogger(new Logger(Config.createConfig(getDataFolder() + "/logs/general", "latest.yml"), Bukkit.getConsoleSender()));
            Logger.setChat(new Logger(Config.createConfig(getDataFolder() + "/logs/chat", "latest.yml"), Bukkit.getConsoleSender()));
            plugin = LoupGarouUHC.getPlugin(LoupGarouUHC.class);

            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "Starting up !");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "Loup Garou UHC plugin (version " + plugin.getDescription().getVersion() + ") by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "If you have any issues, please report it");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.AQUA + "--------------------------------------");

            Logger.getGeneralLogger().log("Loading default game");
            parameters = new Parameters();
            GameInstance = new LGGame(parameters);

            Logger.getGeneralLogger().log("Loading default language");
            new Languages("Default", Languages.DEFAULTID, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            Reader reader = new Reader();
            if (!reader.loadAllLanguage())
                Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Error while loading languages configs");

            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Languages");
            loadDefaultRole();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Loading Default Roles");
            registerConfigs();

            GeneralChatI = new GeneralChat();
            GameChatI = new GameChat();
            DeadChatI = new DeadChat();

            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered PlaceHolders");
            RegisterEvents();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Events");
            registerDefaultScenarios();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Loading Default Scenarios");
            RegisterCommands();
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Commands");

            for (String rolesKeys : RolesConfig.getConfigurationSection("Roles").getKeys(false)) {
                roleNumber.put(rolesKeys, RolesConfig.getInt("Roles." + rolesKeys));
            }


            for (Player player : Bukkit.getOnlinePlayers()) {
                UHCPlayer lgp = UHCPlayer.thePlayer(player);
                lgp.loadStats();
                lgp.loadSavedLang();
                players.add(lgp);
                lgp.setChat(GeneralChatI);
                broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
            }

            HoloStats.createHoloStatsScoreboards();


            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.GREEN + "Finished initialisation");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.AQUA + "--------------------------------------");
        } catch (Exception e) {
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.DARK_RED + "Something went wrong, please restart your server");
            Logger.getGeneralLogger().logInConsole(ChatColor.GOLD + "[LG UHC] " + ChatColor.AQUA + "--------------------------------------");
            Logger.getGeneralLogger().log(e);
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        }
        //Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::);

    }


    private void registerDefaultScenarios() {

        ScenarioUtilis.addScenario(PlayerVote.class);
        ScenarioUtilis.addScenario(XPBoost.class);
        ScenarioUtilis.addScenario(VanillaPlus.class);
        ScenarioUtilis.addScenario(TimedCommands.class);
        ScenarioUtilis.addScenario(Timber.class);
        ScenarioUtilis.addScenario(StarterItems.class);
        ScenarioUtilis.addScenario(OreLimiter.class);
        ScenarioUtilis.addScenario(NoFall.class);
        ScenarioUtilis.addScenario(NoClean.class);
        ScenarioUtilis.addScenario(FireLess.class);
        ScenarioUtilis.addScenario(FinalHeal.class);
        ScenarioUtilis.addScenario(CutClean.class);
        ScenarioUtilis.addScenario(AutoBreak.class);
        ScenarioUtilis.addScenario(DeadlyWater.class);
        ScenarioUtilis.addScenario(FastFurnace.class);
        ScenarioUtilis.addScenario(HastyBoys.class);
        ScenarioUtilis.addScenario(LimitedEnchants.class);
        ScenarioUtilis.addScenario(NoPoison.class);
        ScenarioUtilis.addScenario(RodLess.class);
        ScenarioUtilis.addScenario(DoubleJump.class);
        ScenarioUtilis.addScenario(HungerLess.class);
        ScenarioUtilis.addScenario(NoDrown.class);
        ScenarioUtilis.addScenario(FastPlace.class);
        ScenarioUtilis.addScenario(NoAbsorbtion.class);
        ScenarioUtilis.addScenario(HorseLess.class);
        ScenarioUtilis.addScenario(FightDetector.class);
        ScenarioUtilis.addScenario(UHC.class);
        ScenarioUtilis.addScenario(WorldBorder.class);

    }

    private void loadDefaultRole() {
        try {

            registerRole(LoupGarou.getStaticRoleIdentity(), LoupGarou.class);
            registerRole(Villager.getStaticRoleIdentity(), Villager.class);
            registerRole(PetiteFille.getStaticRoleIdentity(), PetiteFille.class);
            registerRole(VPLoup.getStaticRoleIdentity(), VPLoup.class);
            registerRole(Voyante.getStaticRoleIdentity(), Voyante.class);
            registerRole(LGBlanc.getStaticRoleIdentity(), LGBlanc.class);
            registerRole(MontreurOurs.getStaticRoleIdentity(), MontreurOurs.class);
            registerRole(Assassin.getStaticRoleIdentity(), Assassin.class);
            registerRole(LoupPerfide.getStaticRoleIdentity(), LoupPerfide.class);
            registerRole(Sorciere.getStaticRoleIdentity(), Sorciere.class);
            registerRole(Guard.getStaticRoleIdentity(), Guard.class);
            registerRole(LGInfect.getStaticRoleIdentity(), LGInfect.class);
            registerRole(Cupidon.getStaticRoleIdentity(), Cupidon.class);
            registerRole(LGFeutre.getStaticRoleIdentity(), LGFeutre.class);
            registerRole(Mineur.getStaticRoleIdentity(), Mineur.class);
            registerRole(Renard.getStaticRoleIdentity(), Renard.class);
            registerRole(Detective.getStaticRoleIdentity(), Detective.class);
            registerRole(Corbeau.getStaticRoleIdentity(), Corbeau.class);
            registerRole(Citoyen.getStaticRoleIdentity(), Citoyen.class);
            //registerRole(Salvator.getStaticRoleIdentity(), Salvator.class);
            //todo livre des roles

        } catch (SecurityException e) {
            Logger.getGeneralLogger().log("Error while enabling roles");
            Logger.getGeneralLogger().log(e);
        }
    }

    @Override
    public void onDisable() {
        Logger.getGeneralLogger().archive();
        Logger.getChat().archive();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(onlinePlayer);
            if (lgp.isInGame())
                lgp.getGameData().getGame().endGame(WinType.NONE, true);
        }
        try {
            for (RoleIdentity role : Role.getRoleLinkByStringKey().keySet()) {
                RolesConfig.set("Roles." + role.getId(), 0);
                if (roleNumber.containsKey(role.getId()) && roleNumber.get(role.getId()) != 0) {
                    RolesConfig.set("Roles." + role.getId(), roleNumber.get(role.getId()));
                }
            }

        } catch (Exception err) {
            Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
            Logger.getGeneralLogger().log(err);
        }
        try {
            RolesConfig.save(RoleConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UHCPlayer lgp = UHCPlayer.thePlayer(onlinePlayer);
            lgp.saveLang();
            lgp.saveStats();
        }

        for (int i = 0; i < HoloStats.getCachedHolo().size(); i++) {
            HoloStats holo = HoloStats.getCachedHolo().get(i);
            holo.remove();
            i--;
        }
    }

}