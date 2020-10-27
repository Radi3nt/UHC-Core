/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc;

import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.chats.DeadChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GameChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GeneralChat;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.lang.Reader;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.param.Parameters;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.commands.LGTabCompleter;
import fr.radi3nt.loupgarouuhc.commands.LGcommands;
import fr.radi3nt.loupgarouuhc.event.customlisteners.*;
import fr.radi3nt.loupgarouuhc.event.listeners.*;
import fr.radi3nt.loupgarouuhc.event.listeners.gui.ClickEvent;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou.*;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Solo.Assassin;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.*;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.ScenarioCommands;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.ScenarioListener;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.*;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioUtilis;
import fr.radi3nt.loupgarouuhc.utilis.Config;
import fr.radi3nt.loupgarouuhc.utilis.Updater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.bukkit.Bukkit.broadcastMessage;

public final class LoupGarouUHC extends JavaPlugin implements Listener {

    public static Parameters parameters;

    public static Chat GeneralChatI;
    public static Chat GameChatI;
    public static Chat DeadChatI;

    private static final HashMap<String, Integer> roleNumber = new HashMap<>();
    private static final ArrayList<LGPlayer> players = new ArrayList<>();
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private static final String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + ChatColor.BOLD + "LG UHC" + ChatColor.AQUA + "]" + ChatColor.RESET;
    private static final String prefixPrivé = ChatColor.BLUE + "[Privé]" + ChatColor.RESET;
    private static Plugin plugin;
    private static FileConfiguration RolesConfig;
    private static Updater updater;
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
            e.printStackTrace();
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


        getServer().getPluginManager().registerEvents(new SmallFeaturesListener(), this);
    }

    public static void registerRole(RoleIdentity identity, Class<? extends Role> role) {
        try {
            Role.getRoleLinkByStringKey().put(identity, role.getConstructor(LGGame.class));
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }

    public static ConsoleCommandSender getConsole() {
        return console;
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

    public static ArrayList<LGPlayer> getPlayers() {
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

    @Override
    public void onEnable() {

        new File(getDataFolder() + "/logs", "latest.yml").delete();

        new Logger(Config.createConfig(getDataFolder() + "/logs", "latest.yml"));
        plugin = LoupGarouUHC.getPlugin(LoupGarouUHC.class);

        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "Starting up !");
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "Loup Garou UHC plugin (version " + plugin.getDescription().getVersion() + ") by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "If you have any issues, please report it");
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.AQUA + "--------------------------------------");

        parameters = new Parameters();
        GameInstance = new LGGame(parameters);

        new Languages("Default", Languages.DEFAULTID, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Reader reader = new Reader();
        if (!reader.loadAllLanguage())
            console.sendMessage(ChatColor.DARK_RED + "Error while loading languages configs");

        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Languages");
        loadDefaultRole();
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Loading Default Roles");
        registerConfigs();

        HoloStats.createHoloStatsScoreboards();

        GeneralChatI = new GeneralChat();
        GameChatI = new GameChat();
        DeadChatI = new DeadChat();

        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered PlaceHolders");
        RegisterEvents();
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Events");
        RegisterScenarios();
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Loading Default Scenarios");
        RegisterCommands();
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Commands");

        for (String rolesKeys : RolesConfig.getConfigurationSection("Roles").getKeys(false)) {
            roleNumber.put(rolesKeys, RolesConfig.getInt("Roles." + rolesKeys));
        }


        for (Player player : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(player);
            lgp.loadStats();
            lgp.loadSavedLang();
            players.add(lgp);
            lgp.setChat(GeneralChatI);
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        }

        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.GREEN + "Finished initialisation");
        console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.AQUA + "--------------------------------------");

    }

    private void RegisterScenarios() {

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


        ScenarioListener listen = new ScenarioListener();
        RegisteredListener registeredListener = new RegisteredListener(listen, (listener, event) -> listen.onEvent(event), EventPriority.NORMAL, this, true);
        for (HandlerList handler : HandlerList.getHandlerLists())
            handler.register(registeredListener);
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

        } catch (SecurityException e) {
            System.out.println("Error while enabling roles");
        }
    }

    @Override
    public void onDisable() {
        Logger.getLogger().archive();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
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
            broadcastMessage("§4§lUne erreur est survenue lors de la sauvegarde des roles... Regardez la console !");
            err.printStackTrace();
        }
        try {
            RolesConfig.save(RoleConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
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