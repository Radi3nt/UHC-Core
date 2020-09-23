package fr.radi3nt.loupgarouuhc.classes.game;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.Reason;
import fr.radi3nt.loupgarouuhc.classes.param.Parameters;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleType;
import fr.radi3nt.loupgarouuhc.classes.roles.WinType;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Stats;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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


    private final SecureRandom random = new SecureRandom();

    private Parameters parameters;
    private GameTimer gameTimer;

    private LGGameData data;

    private Boolean isStarted = false;
    private Boolean compoCaché = false;

    private ArrayList<Role> roles = new ArrayList<>();
    private ArrayList<Role> rolesWithDeads = new ArrayList<>();

    private ArrayList<LGPlayer> gamePlayers = new ArrayList<>();
    private ArrayList<LGPlayer> gamePlayersWithDeads = new ArrayList<>();

    private HashMap<LGPlayer, LGPlayer> voted = new HashMap<>();


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
        this.parameters=parameters;
        this.data =new LGGameData();
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
                broadcastMessage("§4§lLa partie ne peut pas commencer, pas assez de joueurs ! (" + playerSize + "/" + i[0] + ")");
            }
        }
    }

    public void start() {

        spawn=parameters.getSpawn();
        gameSpawn=parameters.getGameSpawn();
        for(World w : Bukkit.getWorlds()){
            if(w.getName().equals("game")){
                gameSpawn.setWorld(w);
            }
        } //TODO rahhhhhhhhh
        radius=parameters.getBaseRadius();

        //Registering roles
        for (LGPlayer player : gamePlayers) {
            if (player.getGame() != null) {
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
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_RED + " " + ChatColor.BOLD + "Il faut au moins 1 role pour commencer !"); //TODO 2 roles
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
            lgp.setGame(this);
        }

        Bukkit.broadcastMessage(prefix + ChatColor.DARK_RED + " " + ChatColor.BOLD + "Merci de ne pas vous déconnecter avant le debut de la partie");


        new BukkitRunnable() {
            int timeLeft = 5 * 2;
            int actualRole = RoleSort.values().length;

            @Override
            public void run() {
                if (--timeLeft == 0) {
                    cancel();
                    _start(roles);
                    return;
                }
                if (timeLeft == 5 * 2 - 1) {
                    for (LGPlayer lgp : gamePlayers) {
                        lgp.sendMessage("§8Plugin développé par : §e§lRadi3nt§8.\n");
                        lgp.sendTitle("§6§lLoup Garou UHC", "§8§8By Radi3nt", 5, 60, 5);
                        lgp.getPlayer().getInventory().clear();
                        lgp.getPlayer().updateInventory();
                    }
                    gameSpawn.getWorld().setTime(23250);
                    gameSpawn.getWorld().setFullTime(23250);
                }

                if (--actualRole < 0)
                    actualRole = RoleSort.values().length - 1;
            }
        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 0, 4);
    }

    private void _start(ArrayList<Role> roles) {
        isStarted = false;
        //Give roles...
        ArrayList<LGPlayer> toGive = (ArrayList<LGPlayer>) gamePlayers.clone();
        rolesWithDeads = (ArrayList<Role>) roles.clone();

        gameSpawn.getWorld().getWorldBorder().setSize(radius*10);
        gameSpawn.getWorld().getWorldBorder().setCenter(gameSpawn);
        for (Role role : roles) {
            int randomized = random.nextInt(toGive.size());
            LGPlayer player = toGive.remove(randomized);
            player.setDead(false);
            player.setDiamondMined(0);
            player.setKiller(null);
            player.setCanBeRespawned(false);
            player.setCanVote(false);
            player.setChat(GameChatI);

            role.join(player, false);


            Player p = player.getPlayer();
            p.setInvulnerable(true);
            p.getPlayer().setGameMode(GameMode.ADVENTURE);
            p.resetMaxHealth();
            p.setFoodLevel(20);
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

            Bukkit.broadcastMessage(prefix + ChatColor.GRAY +" Téléportaion de " + ChatColor.WHITE + player.getName());
            for (LGPlayer player1 : gamePlayersWithDeads) {
                player1.getPlayer().playSound(player1.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 100, 1);
            }


            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));
            p.setInvulnerable(false);

            gameSpawn.getWorld().setTime(23250);
            gameSpawn.getWorld().setFullTime(23250);
        }
        gameSpawn.getWorld().getWorldBorder().setSize(radius*2);
        for (LGPlayer lgps : gamePlayersWithDeads) {
            if (lgps.getRole()==null) {
                lgps.setGame(null);
                lgps.setDead(true);
                lgps.setChat(DeadChatI);
                lgps.sendTitle(ChatColor.RED + "You can only spectate the game", ChatColor.GRAY + "You must wait to the game to finish", 5, 2*20, 5);
            }
        }
        GameTimer timer = new GameTimer(this);
        timer.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1L, 0L);
        gameTimer = timer;
        isStarted = true;

    }

    public void scatter(LGPlayer player) {
        Location location = new Location(gameSpawn.getWorld(), Math.random() * radius*2 +gameSpawn.getX() - radius, 0, Math.random() * radius*2+gameSpawn.getX() - radius);
        int y = gameSpawn.getWorld().getHighestBlockYAt(location);
        location.setY(y-2);
        while (location.getBlock().getType() == Material.WATER || location.getBlock().getType()==Material.LAVA || location.getBlock().getBiome() == Biome.OCEAN) {
            location = new Location(gameSpawn.getWorld(), Math.random() * radius*2 +gameSpawn.getX() - radius, 0, Math.random() * radius*2+gameSpawn.getX() - radius);
            int y1 = gameSpawn.getWorld().getHighestBlockYAt(location);
            location.setY(y1-2);
        }
        player.getPlayer().teleport(new Location(location.getWorld(), location.getX()+0.5, location.getY()+2, location.getZ()+0.5));

    }

    public void kill(LGPlayer killed, Reason reason, boolean endGame, Location playerloc) {
        if (killed.getPlayer() != null) {
            roles.remove(killed.getRole());
            gamePlayers.remove(killed);
            killed.setChat(DeadChatI);
            killed.getPlayer().setGameMode(GameMode.SPECTATOR);
            //Lightning effect
            killed.getPlayer().getWorld().strikeLightningEffect(playerloc);
            killed.setDead(true);
            killed.setDiamondMined(0);
            if (killed.getCouple()!=null) {
                killed.getCouple().getPlayer().damage(20);
                killed.getCouple().setCouple(null);
                killed.setCouple(null);
            }
            killed.setGame(null);
            updateKill(endGame);
        }
    }

    public void updateKill(Boolean endGame) {
        if (endGame) {
            endGame(null);
            isStarted=false;
        }

        if (roles.size()<=1) {
            if (roles.isEmpty()) {
                endGame(null);
            } else {
                endGame(roles.get(0).getWinType());
            }
            isStarted=false;
        } else {
            WinType winrole = gamePlayers.get(0).getRole().getWinType();
            if (winrole!=WinType.SOLO) {
                for (LGPlayer player : gamePlayers) {
                    if (player.getRole().getWinType()!=winrole) {
                        return;
                    }
                    if (player.getRole().getWinType()==WinType.SOLO) {
                        return;
                    }
                }
                endGame(roles.get(0).getWinType());
                isStarted = false;
            }
        }
    }


    public void endGame(WinType winType) {

        ArrayList<LGPlayer> winners = new ArrayList<LGPlayer>();
        gameTimer.cancel();

        for(LGPlayer lgp : gamePlayersWithDeads) {//Avoid bugs
            if (lgp.getPlayer() != null) {
                lgp.getPlayer().closeInventory();
                if (lgp.getRole()!=null && lgp.getRoleWinType()==winType && winType!=WinType.SOLO) {
                    winners.add(lgp);
                }
                if (lgp.getRole()!=null && lgp.getRoleWinType()==winType) {
                    if (!lgp.isDead()) {
                        winners.add(lgp);
                    }
                }
                lgp.getPlayer().setGameMode(Bukkit.getServer().getDefaultGameMode());
                lgp.getPlayer().teleport(spawn);
            }
            lgp.setChat(GeneralChatI);
        }
        for (LGPlayer lgp : players) {
            lgp.setChat(GeneralChatI);
        }


        if (winType==null) {
            winType=WinType.NONE;
        }

        Bukkit.broadcastMessage(prefix + " " + ChatColor.BLUE + winType.getMessage());
        for (Role role : rolesWithDeads) {
            if (roles.contains(role)) {
                Bukkit.broadcastMessage(role.getName());
            } else {
                Bukkit.broadcastMessage(ChatColor.STRIKETHROUGH + role.getName());
            }
        }
        for(LGPlayer lgp : gamePlayersWithDeads) {
            Stats stats = lgp.getStats();
            stats.setGameNumber(stats.getGameNumber()+1);
            stats.setPoints(stats.getPoints()+5);

            if (lgp.getPlayer()!=null) {
                lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);

                if (winners.contains(lgp)) {
                    lgp.sendTitle("§a§lVictoire !", "§6Vous avez gagné la partie.", 5, 200, 5);
                    stats.setWinnedGames(stats.getWinnedGames() + 1);
                    if (lgp.isDead()) {
                        stats.setPoints(stats.getPoints() + 10);
                    } else {
                        stats.setPoints(stats.getPoints() + 15);
                    }
                } else if (winType == WinType.EQUAL || winType == WinType.NONE) {
                    lgp.sendTitle("§7§lÉgalité", "§8Personne n'a gagné...", 5, 200, 5);
                } else {
                    lgp.sendTitle("§c§lDéfaite...", "§4Vous avez perdu la partie.", 5, 200, 5);
                }

                lgp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                lgp.getPlayer().getInventory().clear();


                Player p = lgp.getPlayer();
                p.resetMaxHealth();
                p.setFoodLevel(20);
                p.setSaturation(20);
                p.setHealth(20);
                p.setWalkSpeed(0);
                p.setGameMode(Bukkit.getDefaultGameMode());
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
            stats.setKills(stats.getKills()+lgp.getKills());
            stats.setPoints(stats.getPoints()+lgp.getKills()*5);

            lgp.setStats(stats);
            lgp.setKills(0);
            lgp.setDiamondMined(0);
            lgp.setGame(null);
            lgp.setCouple(null);
            lgp.setKiller(null);
            lgp.saveStats();
        }

        HoloStats.updateAll();
        gamePlayers.clear();
        gamePlayersWithDeads.clear();
        GameInstance=new LGGame(parameters);
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

    public Boolean getCompoCaché() {
        return compoCaché;
    }

    public void setCompoCaché(Boolean compoCaché) {
        this.compoCaché = compoCaché;
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

}