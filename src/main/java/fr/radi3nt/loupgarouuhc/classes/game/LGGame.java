package fr.radi3nt.loupgarouuhc.classes.game;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.SystemPlaceHolder;
import fr.radi3nt.loupgarouuhc.classes.param.Parameters;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.player.PlayerGameData;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Stats;
import fr.radi3nt.loupgarouuhc.event.events.OnEndGame;
import fr.radi3nt.loupgarouuhc.event.events.OnStartGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.PvP;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;
import static org.bukkit.Bukkit.broadcastMessage;

public class LGGame {


    public static final int GAME_POINTS = 5;
    public static final int KILLS_POINTS = 5;
    public static final int ALIVE_POINTS = 10;
    public static final int DEAD_POINTS = 5;
    private final ArrayList<Scenario> scenarios = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();

    private Parameters parameters;
    private GameTimer gameTimer;

    private LGGameData data;
    private boolean isStarted = false;

    private final ArrayList<LGPlayer> spectators = new ArrayList<>(); //todo spactators
    private ArrayList<LGPlayer> gamePlayers = new ArrayList<>();
    private ArrayList<LGPlayer> gamePlayersWithDeads = new ArrayList<>();
    private ArrayList<Role> roles = new ArrayList<>();
    private ArrayList<Role> rolesWithDeads = new ArrayList<>();

    private HashMap<LGPlayer, LGPlayer> voted = new HashMap<>();
    private PvP PvP;


    /**
     * Spawn of the world
     */
    private Location spawn;
    /**
     * Spawn of the game
     */
    private Location gameSpawn;
    private Integer radius;


    public LGGame(Parameters parameters) {
        this.parameters = parameters;
        this.data = new LGGameData();
        ArrayList<Integer> timerAlerts = new ArrayList<>();
        timerAlerts.add(25 * 60 * 20);
        timerAlerts.add(20 * 60 * 20);
        timerAlerts.add(10 * 60 * 20);
        PvP = new PvP(this, 30 * 60 * 20, timerAlerts);
        PvP.activate();

        //scenarios.add(new Timber(this,true, 3L));
        //scenarios.add(new NoFall(this));
        //scenarios.add(new CutClean(this));
        /*
        scenarios.add(new VanillaPlus(this, 10, 20));
        scenarios.add(new FinalHeal(this, 20 * 60 * 20));
        HashMap<Material, Integer> xp = new HashMap<>();
        //xp.put(Material.IRON_ORE, 2);
        //xp.put(Material.GOLD_ORE, 3);
        scenarios.add(new XPBoost(this, 10, xp));
        //scenarios.add(new AutoBreak(this, 20*60, AutoBreak.MODE_BLACKLIST, new ArrayList<>()));
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.COOKED_BEEF, 32));
        scenarios.add(new StarterItems(this, items));
        ArrayList<String> strings = new ArrayList<>();
        strings.add("pa leave");
        strings.add("toggleeditwand");
        scenarios.add(new TimedCommands(this, strings, new ArrayList<>(), strings));
        HashMap<Material, Integer> ores = new HashMap<>();
        ores.put(Material.DIAMOND, 17);
        scenarios.add(new OreLimiter(this, ores));
        //scenarios.add(new FireLess(this, 50));
        //scenarios.add(new NoClean(this, 5*20));

         */
    }

    public void join(LGPlayer player) {
        if (!isStarted) {
            gamePlayers.add(player);
        }
    }

    public void updateStart() {
        if (!isStarted) {
            final int[] i = {0};
            roleNumber.forEach((s, integer) -> i[0] = i[0] +integer);
            int playerSize = gamePlayers.size();
            if (playerSize == i[0]) {
                start();
            } else {
                isStarted = false;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
                    lgp.sendMessage(lgp.getLanguage().getMessage("gameCannotStartPlayers").replace("%playerSize%", String.valueOf(playerSize)).replace("%roleSize%", String.valueOf(i[0])));
                }
            }
        }
    }

    public void start() {

        spawn = parameters.getSpawn().clone();
        gameSpawn = parameters.getGameSpawn().clone();
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().equals("game")) {
                gameSpawn.setWorld(w);
            }
        } //TODO rahhhhhhhhh
        radius = parameters.getBaseRadius();

        //Registering roles
        for (LGPlayer player : gamePlayers) {
            if (player.isInGame()) {
                gamePlayers.remove(player);
                updateStart();
                return;
            }
        }

        gamePlayersWithDeads= (ArrayList<LGPlayer>) gamePlayers.clone();
        try {
            for (Map.Entry<String, Constructor<? extends Role>> role : rolesLink.entrySet()) {
                for (int i = 0; i < roleNumber.getOrDefault(role.getKey(), 0); i++) {
                    roles.add(role.getValue().newInstance(this));
                }
                RolesConfig.set("Roles." + role.getKey(), 0);
            }
        } catch (Exception err) {
            broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
            err.printStackTrace();
        }

        // Canceler
        if (roles.isEmpty()) {
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_RED + " " + ChatColor.BOLD + "Il faut au moins 1 role pour commencer !"); //TODO 2 roles + message
            gamePlayers.clear();
            gamePlayersWithDeads.clear();
            roles.clear();
            isStarted=false;
            return;
        }

        RoleType type = roles.get(0).getRoleType();
        boolean same = false;
        for (Role role : roles) {
            if (role.getRoleType()!=type) {
                same=true;
            }
        }
        same=true; //todo a enlever en game
        if (!same) {
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_RED + " " + ChatColor.BOLD + "Il faut au moins 2 sortes de role pour commencer !");
            gamePlayers.clear();
            gamePlayersWithDeads.clear();
            roles.clear();
            isStarted=false;
            return;
        }

        //The game can start !
        gameSpawn.getWorld().setTime(23250);
        gameSpawn.getWorld().setFullTime(23250);

        for (LGPlayer lgp : gamePlayers) {
            lgp.setGameData(new PlayerGameData(this));
            lgp.getGameData().setGame(this);
            lgp.sendMessage(lgp.getLanguage().getMessage("gameStartDisconnectMessage", lgp));
        }


        gameSpawn.getWorld().getWorldBorder().setSize(100000);
        final boolean[] canStart = {true};
        if (parameters.isAutoMap()) {
            canStart[0] = false;
            new BukkitRunnable() {

                @Override
                public void run() {
                    gameSpawn = findRoofedForest(gameSpawn, 1000, 2500);
                    canStart[0] = true;
                    cancel();
                }
            }.runTaskLater(plugin, 1);
        }

        new BukkitRunnable() {
            int timeOut = 20 * 20;
            int timeLeft = 5 * 2;
            int actualRole = RoleSort.values().length;

            @Override
            public void run() {
                if (canStart[0] || timeOut == 0) {
                    if (--timeLeft == 0) {
                        cancel();
                        startGameForReal(roles);
                        return;
                    }
                    if (timeLeft == 5 * 2 - 1) {
                        for (LGPlayer lgp : gamePlayers) {
                            String message = lgp.getLanguage().getMessage("gameStartAuthorMessage");
                            if (!message.contains("%author%")) {
                                message = message + " " + SystemPlaceHolder.getMessageReplaced("%author%", lgp);
                                lgp.sendMessage(message);
                            } else {
                                lgp.sendMessage(lgp.getLanguage().getMessage("gameStartAuthorMessage", lgp));
                            }
                            lgp.sendTitle(lgp.getLanguage().getMessage("gameStartAuthorTitle", lgp), lgp.getLanguage().getMessage("gameStartAuthorSubtitle", lgp), 5, 60, 5);
                            lgp.getPlayer().getInventory().clear();
                            lgp.getPlayer().updateInventory();
                        }
                        gameSpawn.getWorld().setTime(23250);
                        gameSpawn.getWorld().setFullTime(23250);
                    }

                    if (--actualRole < 0)
                        actualRole = RoleSort.values().length - 1;
                } else {
                    timeOut--;
                }
            }
        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 0, 4);
    }

    private void startGameForReal(ArrayList<Role> roles) {
        isStarted = false;
        //Give roles...
        ArrayList<LGPlayer> toGive = (ArrayList<LGPlayer>) gamePlayers.clone();
        rolesWithDeads = (ArrayList<Role>) roles.clone();

        for (Role role : roles) {
            int randomized = random.nextInt(toGive.size());
            LGPlayer player = toGive.remove(randomized);
            player.getGameData().setDead(false);
            player.getGameData().setKiller(null);
            player.getGameData().setCanBeRespawned(false);
            player.getGameData().setCanVote(false);
            player.setChat(GameChatI);

            role.join(player, false);


            Player p = player.getPlayer();
            p.setInvulnerable(true);
            p.getPlayer().setGameMode(GameMode.ADVENTURE);
            p.resetMaxHealth();
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setHealth(20);
            p.setWalkSpeed(0);
            player.getPlayer().setExp(0);
            player.getPlayer().setLevel(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                try {
                    player.getPlayer().removePotionEffect(ption);
                } catch (Exception e) {

                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 180, true, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 8, true, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 0, true, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 5, true, false));
            scatter(player);

            for (LGPlayer player1 : gamePlayersWithDeads) {
                player1.sendMessage(player1.getLanguage().getMessage("gameStartTp").replace("%playerName%", player.getName()));
                player1.getPlayer().playSound(player1.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 100, 1);
            }

            p.getInventory().clear();
            p.updateInventory();

            p.setInvulnerable(false);

            gameSpawn.getWorld().setTime(23250);
            gameSpawn.getWorld().setFullTime(23250);
        }
        gameSpawn.getWorld().getWorldBorder().setCenter(gameSpawn);
        gameSpawn.getWorld().getWorldBorder().setSize(parameters.getBaseRadius() * 2);
        for (LGPlayer lgps : gamePlayersWithDeads) {
            if (!lgps.getGameData().hasRole()) {
                lgps.getGameData().setGame(null);
                lgps.getGameData().setDead(true);
                lgps.setChat(DeadChatI);
                lgps.sendTitle(ChatColor.RED + "You can only spectate the game", ChatColor.GRAY + "You must wait to the game to finish", 5, 2 * 20, 5);
            }
        }
        GameTimer timer = new GameTimer(this);
        timer.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1L, 0L);
        gameTimer = timer;
        isStarted = true;
        //Scenarios part

        for (Scenario scenario : scenarios) {
            scenario.activate();
        }

        Bukkit.getPluginManager().callEvent(new OnStartGame(this));
    }

    public void scatter(LGPlayer player) {
        Location location = new Location(gameSpawn.getWorld(), Math.random() * 1000 + gameSpawn.getBlockX() - radius, 0, Math.random() * 1000 + gameSpawn.getBlockZ() - radius);
        int y = gameSpawn.getWorld().getHighestBlockYAt(location);
        location.setY(y - 1);
        int i = (radius * radius * 2 * 2) * 4;
        while (!location.getBlock().getType().isSolid() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getBiome() == Biome.OCEAN) {
            location = new Location(gameSpawn.getWorld(), Math.random() * 1000 + gameSpawn.getBlockX() - radius, 0, Math.random() * 1000 + gameSpawn.getBlockZ() - radius);
            int y1 = gameSpawn.getWorld().getHighestBlockYAt(location);
            location.setY(y1 - 1);
            if (i == 0)
                break;

            i--;
        }
        player.getPlayer().teleport(new Location(location.getWorld(), location.getBlockX() + 0.5, gameSpawn.getWorld().getHighestBlockYAt(location), location.getBlockZ() + 0.5));
        player.getPlayer().setGravity(true);
    }

    public Location findRoofedForest(Location baseLoc1, int timeout, int distance) {
        if (baseLoc1.getBlock().getBiome() != Biome.ROOFED_FOREST) {
            Location baseLoc = baseLoc1.clone();
            int i = 0;
            SecureRandom random = new SecureRandom();


            baseLoc.add(random.nextInt(distance) - (float) distance / 2, 0, random.nextInt(distance) - (float) distance / 2);
            while (baseLoc.getBlock().getBiome() != Biome.ROOFED_FOREST) {
                baseLoc.add(16, 0, 16);
                if (i >= timeout) {
                    System.out.println("Cannot find roofed forest");
                    return baseLoc1;
                }
                i++;
            }
            return baseLoc;
        }
        return baseLoc1;
    }

    public void kill(LGPlayer killed, Reason reason, boolean endGame, Location playerloc) {
        if (killed.getPlayer() != null) {
            roles.remove(killed.getGameData().getRole());
            gamePlayers.remove(killed);
            killed.setChat(DeadChatI);
            killed.getPlayer().setGameMode(GameMode.SPECTATOR);
            //Lightning effect
            killed.getPlayer().getWorld().strikeLightningEffect(playerloc);
            killed.getGameData().setDead(true);
            if (killed.getGameData().getCouple() != null) {
                killed.getGameData().getCouple().getPlayer().damage(20);
                killed.getGameData().setCouple(null);
            }
            killed.getGameData().setGame(null);
            updateKill(endGame);
        }
    }

    public void updateKill(Boolean endGame) {
        if (endGame) {
            endGame(null, false);
            isStarted = false;
        }

        if (roles.size()<=1) {
            if (roles.isEmpty()) {
                endGame(null, false);
            } else {
                endGame(roles.get(0).getWinType(), false);
            }
            isStarted=false;
        } else {
            WinType winrole = gamePlayers.get(0).getGameData().getRole().getWinType();
            if (winrole!=WinType.SOLO) {
                for (LGPlayer player : gamePlayers) {
                    if (player.getGameData().isInfected() && winrole == WinType.LOUP_GAROU)
                        continue;
                    if (player.getGameData().getRole().getWinType() != winrole)
                        return;
                    if (player.getGameData().getRole().getWinType() == WinType.SOLO)
                        return;
                }
                endGame(roles.get(0).getWinType(), false);
                isStarted = false;
            }
        }
    }

    public void endGame(WinType winType, boolean fast) {

        Bukkit.getPluginManager().callEvent(new OnEndGame(this));

        for (Scenario scenario : scenarios) {
            scenario.deactivate();
            scenario.unregister();
        }

        ArrayList<LGPlayer> winners = new ArrayList<>();
        gameTimer.cancel();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
            lgp.setChat(GeneralChatI);
        }

        if (winType == null) {
            winType = WinType.NONE;
        }

        for (LGPlayer lgp : gamePlayersWithDeads) {
            lgp.setGameData(new PlayerGameData(null));
            if (lgp.getPlayer() != null) {
                lgp.getPlayer().closeInventory();
                if (lgp.getGameData().getRole() != null && lgp.getGameData().getRoleWinType() == winType && winType != WinType.SOLO) {
                    winners.add(lgp);
                }
                if (lgp.getGameData().getRole() != null && lgp.getGameData().getRoleWinType() == winType) {
                    if (!lgp.getGameData().isDead()) {
                        winners.add(lgp);
                    }
                }
            }
            Stats stats = lgp.getStats();
            stats.setGameNumber(stats.getGameNumber() + 1);
            stats.setPoints(stats.getPoints() + GAME_POINTS);

            if (lgp.getPlayer() != null) {
                lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);

                if (lgp.getGameData().isDead()) {
                    stats.setPoints(stats.getPoints() + DEAD_POINTS);
                } else {
                    stats.setPoints(stats.getPoints() + ALIVE_POINTS);
                }

                if (winners.contains(lgp)) {
                    lgp.sendTitle("§a§lVictoire !", "§6Vous avez gagné la partie.", 5, 200, 5);
                    stats.setWinnedGames(stats.getWinnedGames() + 1);
                } else if (winType == WinType.EQUAL || winType == WinType.NONE) {
                    lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);
                } else {
                    lgp.sendTitle("§c§lDéfaite...", "§4Vous avez perdu la partie.", 5, 200, 5);
                }

                lgp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                stats.setKills(stats.getKills() + lgp.getGameData().getKills());
                stats.setPoints(stats.getPoints() + lgp.getGameData().getKills() * KILLS_POINTS);

                lgp.setStats(stats);
            }
        }


        if (fast) {
            parameters.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
            parameters.getGameSpawn().getWorld().getWorldBorder().setCenter(parameters.getGameSpawn());
            for (LGPlayer lgp : gamePlayersWithDeads) {
                resetPlayer(lgp);
            }
            parameters.getGameSpawn().getWorld().getWorldBorder().setSize(parameters.getBaseRadius() * 2);
            gamePlayers.clear();
            gamePlayersWithDeads.clear();
            if (parameters.isRecreateMapAfterUse()) {
                if (gameSpawn.getWorld().getPlayers().isEmpty()) {
                    String name = gameSpawn.getWorld().getName();
                    Bukkit.getServer().unloadWorld(gameSpawn.getWorld(), true);
                    gameSpawn.getWorld().getWorldFolder().delete();

                    WorldCreator wc = new WorldCreator(name);
                    wc.environment(World.Environment.NORMAL);
                    wc.type(WorldType.NORMAL);
                    gameSpawn.setWorld(wc.createWorld());
                } else {
                    for (Player player : gameSpawn.getWorld().getPlayers()) {
                        player.kickPlayer("Sorry, something went wrong");
                    }
                    if (gameSpawn.getWorld().getPlayers().isEmpty()) {
                        String name = gameSpawn.getWorld().getName();
                        Bukkit.getServer().unloadWorld(gameSpawn.getWorld(), true);
                        gameSpawn.getWorld().getWorldFolder().delete();

                        WorldCreator wc = new WorldCreator(name);
                        wc.environment(World.Environment.NORMAL);
                        wc.type(WorldType.NORMAL);
                        gameSpawn.setWorld(wc.createWorld());
                    } else {
                        console.sendMessage(ChatColor.DARK_RED + "Something went horribly wrong ! Please restart your server and change manually the game map");
                    }
                }
            }
        } else {
            new BukkitRunnable() {
                final int deleteTimeout = 60 * 20;
                int i = 0;
                int deleteTime = 11 * 20;

                @Override
                public void run() {
                    i++;
                    if (i == 10 * 20) {
                        parameters.getGameSpawn().getWorld().getWorldBorder().setSize(10000000);
                        parameters.getGameSpawn().getWorld().getWorldBorder().setCenter(parameters.getGameSpawn());
                        for (LGPlayer lgp : gamePlayersWithDeads) {
                            resetPlayer(lgp);
                        }
                        parameters.getGameSpawn().getWorld().getWorldBorder().setSize(parameters.getBaseRadius() * 2);
                        gamePlayers.clear();
                        gamePlayersWithDeads.clear();
                    }
                    if (i == deleteTime) {
                        if (parameters.isRecreateMapAfterUse()) {
                            if (gameSpawn.getWorld().getPlayers().isEmpty()) {
                                String name = gameSpawn.getWorld().getName();
                                Bukkit.getServer().unloadWorld(gameSpawn.getWorld(), true);
                                gameSpawn.getWorld().getWorldFolder().delete();

                                WorldCreator wc = new WorldCreator(name);
                                wc.environment(World.Environment.NORMAL);
                                wc.type(WorldType.NORMAL);
                                World world = wc.createWorld();
                                gameSpawn.setWorld(world);
                                parameters.getGameSpawn().setWorld(world);
                                cancel();
                            } else {
                                deleteTime += 20 * 5;
                            }
                        } else {
                            cancel();
                        }
                    }
                    if (i == deleteTimeout) {
                        System.out.println("Cannot delete map");
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }

        Bukkit.broadcastMessage(prefix + " " + ChatColor.BLUE + winType.getMessage());
        for (Role role : rolesWithDeads) {
            if (roles.contains(role)) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
                    lgp.sendMessage(role.getName(lgp.getLanguage()));
                }
            } else {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    LGPlayer lgp = LGPlayer.thePlayer(onlinePlayer);
                    lgp.sendMessage(ChatColor.STRIKETHROUGH + role.getName(lgp.getLanguage()));
                }
            }
        }

        HoloStats.updateAll();
        GameInstance = new LGGame(parameters);
    }

    private void resetPlayer(LGPlayer lgp) {
        if (lgp.getPlayer() != null) {
            lgp.getPlayer().getInventory().clear();
            lgp.getPlayer().teleport(spawn);
            lgp.getPlayer().setGameMode(Bukkit.getServer().getDefaultGameMode());
            Player p = lgp.getPlayer();
            p.resetMaxHealth();
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setHealth(20);
            p.setWalkSpeed(0);
            p.setExp(0);
            p.setLevel(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                try {
                    p.removePotionEffect(ption);
                } catch (Exception e) {

                }
            }
            lgp.getPlayer().setWalkSpeed(0.2F);
        }
        lgp.getGameData().setKills(0);
        lgp.getGameData().setCouple(null);
        lgp.getGameData().setKiller(null);
        lgp.getGameData().setGame(null);
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public HashMap<LGPlayer, LGPlayer> getVoted() {
        return voted;
    }

    public void setVoted(HashMap<LGPlayer, LGPlayer> voted) {
        this.voted = voted;
    }

    public ArrayList<LGPlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(ArrayList<LGPlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public ArrayList<LGPlayer> getGamePlayersWithDeads() {
        return gamePlayersWithDeads;
    }

    public void setGamePlayersWithDeads(ArrayList<LGPlayer> gamePlayersWithDeads) {
        this.gamePlayersWithDeads = gamePlayersWithDeads;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getGameSpawn() {
        return gameSpawn;
    }

    public Integer getRadius() {
        return radius;
    }

    public ArrayList<Role> getRolesWithDeads() {
        return rolesWithDeads;
    }

    public void setRolesWithDeads(ArrayList<Role> rolesWithDeads) {
        this.rolesWithDeads = rolesWithDeads;
    }

    public LGGameData getData() {
        return data;
    }

    public void setData(LGGameData data) {
        this.data = data;
    }

    public LGPlayer getHost() {
        return data.getHost();
    }

    public ArrayList<LGPlayer> getSpectators() {
        return spectators;
    }

    public ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

    public fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.PvP getPvP() {
        return PvP;
    }

    public void setPvP(fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.PvP pvP) {
        PvP = pvP;
    }
}