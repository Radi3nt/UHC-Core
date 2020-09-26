/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.loupgarouuhc;

import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.chats.DeadChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GameChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GeneralChat;
import fr.radi3nt.loupgarouuhc.classes.config.Config;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.lang.LangWarper;
import fr.radi3nt.loupgarouuhc.classes.lang.Language;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.param.Parameters;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.*;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo.Assassin;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers.*;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.commands.LGcommands;
import fr.radi3nt.loupgarouuhc.customlisteners.*;
import fr.radi3nt.loupgarouuhc.listeners.*;
import fr.radi3nt.loupgarouuhc.listeners.gui.ClickEvent;
import fr.radi3nt.loupgarouuhc.utilis.Updater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.broadcastMessage;

public final class LoupGarouUHC extends JavaPlugin {

    public static Parameters parameters;

    public static LangWarper langWarperInstance;

    public static Chat GeneralChatI;
    public static Chat GameChatI;
    public static Chat DeadChatI;

    public static Plugin plugin;

    HashMap<RoleSort, Boolean> MaxRoles = new HashMap<>();

    public static HashMap<String, Integer> roleNumber = new HashMap<>();
    public static ArrayList<LGPlayer> players = new ArrayList<>();
    public static HashMap<String, Constructor<? extends Role>> rolesLink = new HashMap<String, Constructor<? extends Role>>();
    public static FileConfiguration RolesConfig;
    public static File RoleConfigFile;

    public static LGGame GameInstance;
    private static Updater updater;

    // Plugin startup logic
    //--------------------------------------------------//
    public static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    //--------------------------------------------------//

    public static String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + ChatColor.BOLD + "LG UHC" + ChatColor.AQUA + "]" + ChatColor.RESET;
    public static String prefixPrivé = ChatColor.BLUE + "[Privé]" + ChatColor.RESET;


    @Override
    public void onEnable() {

            new Logger(Config.createConfig(getDataFolder() + "/logs", "latest.yml"));
            plugin = LoupGarouUHC.getPlugin(LoupGarouUHC.class);

            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "Starting up !");
            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "LG UHC Plugin by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.YELLOW + "If you have any issues, please report it");

            parameters=new Parameters();
            GameInstance = new LGGame(parameters);


            LangWarper langWarper = new LangWarper();
            langWarper.InitLang(Language.FRANCAIS);
            langWarperInstance = langWarper;

        loadRoles();
        registerConfigs();


        HoloStats.createHoloStatsScoreboards();

            GeneralChatI=new GeneralChat();
            GameChatI=new GameChat();
            DeadChatI=new DeadChat();

            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Languages");

            RegisterEvents();
            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Events");
            RegisterCommands();
            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Commands");
            RegisterRunnables();
            console.sendMessage(ChatColor.GOLD + "[LG UHC] " + ChatColor.RED + "Registered Runnables");


        for (String rolesKeys : RolesConfig.getConfigurationSection("Roles").getKeys(false)) {
            roleNumber.put(rolesKeys, RolesConfig.getInt("Roles." + rolesKeys));
        }


        for (Player player : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(player);
            players.add(lgp);
            lgp.setChat(GeneralChatI);
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
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
        RoleConfigFile =locations;
    }

    private void RegisterRunnables() {

    }

    private void RegisterCommands() {
        getCommand("lg").setExecutor(new LGcommands());
    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(), this);


        getServer().getPluginManager().registerEvents(new OnPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDie(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBreakBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerPreProcessCommand(), this);

        getServer().getPluginManager().registerEvents(new ClickEvent(), this);

        getServer().getPluginManager().registerEvents(new OnDayEvent(), this);
        getServer().getPluginManager().registerEvents(new OnNightEvent(), this);
        getServer().getPluginManager().registerEvents(new OnNewEpisodeEvent(), this);
        getServer().getPluginManager().registerEvents(new OnDiscoverRoleEvent(), this);
        getServer().getPluginManager().registerEvents(new OnKilledEvent(), this);
        getServer().getPluginManager().registerEvents(new OnKillEvent(), this);
    }

    @Override
    public void onDisable() {
        try {
            for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet()) {
                RolesConfig.set("Roles." + role.getKey(), 0);
                if (roleNumber.containsKey(role.getKey()) && roleNumber.get(role.getKey())!=0) {
                    RolesConfig.set("Roles." + role.getKey(), roleNumber.get(role.getKey()));
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

        /*
        Iterator<HoloStats> it = HoloStats.getCachedHolo().iterator();
        while (it.hasNext()) {
            HoloStats holo = it.next();
            holo.remove();
            it.remove();
        }

         */
        for (int i = 0; i < HoloStats.getCachedHolo().size(); i++) {
            HoloStats holo = HoloStats.getCachedHolo().get(i);
            holo.remove();
            i--;
        }
    }
    private void loadRoles() {
        try {
            rolesLink.put(RoleSort.LOUP_GAROU.id, LoupGarou.class.getConstructor(LGGame.class));
            rolesLink.put(RoleSort.VILLAGER.id, Villager.class.getConstructor(LGGame.class));
            rolesLink.put(RoleSort.PETITE_FILLE.id, PetiteFille.class.getConstructor(LGGame.class));
            rolesLink.put("VPLoup", VPLoup.class.getConstructor(LGGame.class));
            rolesLink.put("Voyante", Voyante.class.getConstructor(LGGame.class));
            rolesLink.put("LGBlanc", LGBlanc.class.getConstructor(LGGame.class));
            rolesLink.put("MontreurOurs", MontreurOurs.class.getConstructor(LGGame.class));
            rolesLink.put("Assassin", Assassin.class.getConstructor(LGGame.class));
            rolesLink.put("LoupPerfide", LoupPerfide.class.getConstructor(LGGame.class));
            rolesLink.put("Sorciere", Sorciere.class.getConstructor(LGGame.class));
            rolesLink.put("Guard", Guard.class.getConstructor(LGGame.class));
            rolesLink.put("LGInfect", LGInfect.class.getConstructor(LGGame.class));
            rolesLink.put("Cupidon", Cupidon.class.getConstructor(LGGame.class));
            rolesLink.put("LGFeutre", LGFeutre.class.getConstructor(LGGame.class));
            rolesLink.put("Mineur", Mineur.class.getConstructor(LGGame.class));
        } catch (NoSuchMethodException | SecurityException e) {
            System.out.println("Error while enabling roles");
        }
    }


    public static Parameters getParameters() {
        return parameters;
    }

    public static LangWarper getLangWarperInstance() {
        return langWarperInstance;
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

    public static HashMap<String, Constructor<? extends Role>> getRolesLink() {
        return rolesLink;
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
}