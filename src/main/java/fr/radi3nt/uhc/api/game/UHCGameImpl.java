package fr.radi3nt.uhc.api.game;

import fr.radi3nt.uhc.api.events.UHCGameEndsEvent;
import fr.radi3nt.uhc.api.events.UHCGameStartsEvent;
import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.uhc.ClassicGame;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class UHCGameImpl implements UHCGame {

    private final UUID uuid;
    protected final Parameter parameter;
    protected GameState state = GameState.LOBBY;
    protected GameData data = new GameData();
    protected GameTimer gameTimer;
    protected int radius;

    protected Set<UHCPlayer> alivePlayers = new HashSet<>();
    protected Set<UHCPlayer> players = new HashSet<>();
    protected Set<UHCPlayer> spectators = new HashSet<>();
    protected Set<UHCPlayer> waitQueue = new HashSet<>();

    protected List<Scenario> scenarios = new ArrayList<>();
    protected PvP PvP;

    public UHCGameImpl(Parameter parameter) {
        this.parameter = parameter;
        this.radius = parameter.getBaseRadius();
        this.uuid=UUID.randomUUID();
    }

    protected abstract void _join(UHCPlayer player);

    protected abstract void _spectate(UHCPlayer player);

    protected abstract boolean _updateStart();

    protected abstract void _start();

    protected abstract void _end(List<UHCPlayer> winners, boolean fast);

    protected abstract void _kill(UHCPlayer player, Reason reason, Location playerloc);

    public void join(UHCPlayer player) {
        _join(player);
    }

    @Override
    public void spectate(UHCPlayer player) {
        _spectate(player);
    }

    public boolean updateStart() {
        return _updateStart();
    }

    public void start() {
        state = GameState.STARTING;
        _start();
    }

    protected void initGame() {
        gameTimer.runTaskTimer(UHCCore.getPlugin(), 1L, 1L);
        PvP.activate();
        registerAndActivateAllScenario();
        state = GameState.PLAYING;
        Bukkit.getPluginManager().callEvent(new UHCGameStartsEvent(this));
    }

    public void end(List<UHCPlayer> winners, boolean fast) {
        state = GameState.END;
        data.getLogChat().log("-- Endgame --");
        Bukkit.getPluginManager().callEvent(new UHCGameEndsEvent(this));

        unregisterAndDeactivateAllScenario();

        gameTimer.cancel();
        _end(winners, fast);
        Path log = Paths.get(UHCCore.getPlugin().getDataFolder() + "/logs/game");
        data.getLogChat().archive(log, uuid);
        try {
            Files.delete(data.getLogChat().getConfig().getFile().toPath());
        } catch (IOException e) {
            Logger.getGeneralLogger().log(e);
        }

        HoloStats.updateAll();
        UHCCore.getGameQueue().remove(this);

        UHCGame game = null;
        try {
            game = this.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getGeneralLogger().log(e);
        }
        if (game==null)
            game=new ClassicGame();
        else
            game.getScenarios().addAll(scenarios);

        UHCCore.getGameQueue().add(game);
    }

    public void kill(UHCPlayer player, Reason reason, Location playerloc) {
        _kill(player, reason, playerloc);
    }

    public void scatter(UHCPlayer player) {
        Location location = new Location(parameter.getGameSpawn().getWorld(), Math.random() * 1000 + parameter.getGameSpawn().getBlockX() - ((float) radius / 2), 0, Math.random() * 1000 + parameter.getGameSpawn().getBlockZ() - ((float) radius / 2));
        int y = parameter.getGameSpawn().getWorld().getHighestBlockYAt(location);
        location.setY(y - 1);
        int i = (getRadius() * getRadius() * 2 * 2) * 4;
        while (!location.getBlock().getType().isSolid() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getBiome() == Biome.OCEAN) {
            location = new Location(parameter.getGameSpawn().getWorld(), Math.random() * 1000 + parameter.getGameSpawn().getBlockX() - ((float) radius / 2), 0, Math.random() * 1000 + parameter.getGameSpawn().getBlockZ() - ((float) radius / 2));
            int y1 = parameter.getGameSpawn().getWorld().getHighestBlockYAt(location);
            location.setY(y1 - 1);
            if (i == 0) {
                getData().getLogChat().log("Cannot tp player: " + player.getName() + ", timeout is over !");
                break;
            }

            i--;
        }
        player.getPlayer().teleport(new Location(location.getWorld(), location.getBlockX() + 0.5, parameter.getGameSpawn().getWorld().getHighestBlockYAt(location), location.getBlockZ() + 0.5));
        player.getPlayer().setGravity(true);
        getData().getLogChat().log("Scattered " + player.getName());
    }

    protected void registerAndActivateAllScenario() {
        for (Scenario scenario : scenarios) {
            scenario.register();
            scenario.activate();
            data.getLogChat().log("Scenario: " + scenario.getClass().getName() + " activated");
        }
    }

    protected void unregisterAndDeactivateAllScenario() {
        for (Scenario scenario : scenarios) {
            scenario.deactivate();
            scenario.unregister();
            data.getLogChat().log("Scenario: " + scenario.getClass().getName() + " deactivated");
        }
    }

    public UHCPlayer getUHCPlayerInThisGame(String name) {
        for (UHCPlayer gamePlayer : this.getAlivePlayers()) {
            if (gamePlayer.getName().equals(name)) {
                return gamePlayer;
            }
        }
        return null;
    }

    public GameState getState() {
        return state;
    }

    public GameData getData() {
        return data;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public Parameter getParameters() {
        return parameter;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Set<UHCPlayer> getAlivePlayers() {
        return alivePlayers;
    }

    public Set<UHCPlayer> getSpectators() {
        return spectators;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(Scenario scenario) {
        for (Scenario scenario1 : scenarios) {
            if (scenario.getClass().equals(scenario1.getClass()))
                return;
        }
        scenarios.add(scenario);
    }

    public void forceScenario(Scenario scenario) {
        for (int i = 0; i < scenarios.size(); i++) {
            Scenario scenario1 = scenarios.toArray(new Scenario[0])[i];
            if (scenario.getClass().equals(scenario1.getClass())) {
                scenarios.remove(scenario1);
                i--;
            }
        }
        scenarios.add(scenario);
    }

    public void removeScenario(Scenario scenario) {
        scenarios.remove(scenario);
    }

    protected void clearQueue() {
        for (UHCPlayer uhcPlayer : waitQueue) {
            uhcPlayer.clearWaitingGame();
        }
    }

    @Override
    public Set<UHCPlayer> getWaitQueue() {
        return waitQueue;
    }

    public PvP getPvP() {
        return PvP;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
