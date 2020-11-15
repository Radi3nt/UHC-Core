package fr.radi3nt.uhc.api.game;

import fr.radi3nt.uhc.api.events.UHCGameEndsEvent;
import fr.radi3nt.uhc.api.events.UHCGameStartsEvent;
import fr.radi3nt.uhc.api.game.instances.DefaultsParameters;
import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import fr.radi3nt.uhc.uhc.ClassicGame;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class UHCGameImpl implements UHCGame {

    protected final Parameter parameter = new DefaultsParameters();
    protected GameState state = GameState.LOBBY;
    protected GameData data = new GameData();
    protected GameTimer gameTimer;
    protected int radius = parameter.getBaseRadius();

    protected Set<UHCPlayer> alivePlayers = new HashSet<>();
    protected Set<UHCPlayer> deadPlayers = new HashSet<>();
    protected Set<UHCPlayer> spectators = new HashSet<>();

    protected List<Scenario> scenarios = new ArrayList<>();
    protected PvP PvP;

    protected abstract void _join(UHCPlayer player);

    protected abstract boolean _updateStart();

    protected abstract void _start();

    protected abstract void _end(List<UHCPlayer> winners, boolean fast);

    protected abstract void _kill(UHCPlayer player, Reason reason, Location playerloc);

    public abstract Location getGameSpawn();

    public void join(UHCPlayer player) {
        _join(player);
    }

    public boolean updateStart() {
        return _updateStart();
    }

    public void start() {
        state = GameState.STARTING;
        _start();
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

        for (Scenario scenario : scenarios) {
            scenario.deactivate();
            scenario.unregister();
            data.getLogChat().log("Scenario: " + Scenario.getName() + " deactivated");
        }

        gameTimer.cancel();
        _end(winners, fast);
        UHCCore.getGames().remove(this);
        UHCCore.getGames().add(new ClassicGame());
    }

    public void kill(UHCPlayer player, Reason reason, Location playerloc) {
        _kill(player, reason, playerloc);
    }

    public void scatter(UHCPlayer player) {
        Location location = new Location(getGameSpawn().getWorld(), Math.random() * 1000 + getGameSpawn().getBlockX() - ((float) radius / 2), 0, Math.random() * 1000 + getGameSpawn().getBlockZ() - ((float) radius / 2));
        int y = getGameSpawn().getWorld().getHighestBlockYAt(location);
        location.setY(y - 1);
        int i = (getRadius() * getRadius() * 2 * 2) * 4;
        while (!location.getBlock().getType().isSolid() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getBiome() == Biome.OCEAN) {
            location = new Location(getGameSpawn().getWorld(), Math.random() * 1000 + getGameSpawn().getBlockX() - ((float) radius / 2), 0, Math.random() * 1000 + getGameSpawn().getBlockZ() - ((float) radius / 2));
            int y1 = getGameSpawn().getWorld().getHighestBlockYAt(location);
            location.setY(y1 - 1);
            if (i == 0) {
                getData().getLogChat().log("Cannot tp player: " + player.getName() + ", timeout is over !");
                break;
            }

            i--;
        }
        player.getPlayer().teleport(new Location(location.getWorld(), location.getBlockX() + 0.5, getGameSpawn().getWorld().getHighestBlockYAt(location), location.getBlockZ() + 0.5));
        player.getPlayer().setGravity(true);
        getData().getLogChat().log("Scattered " + player.getName());
    }

    protected void registerAndActivateAllScenario() {
        for (Scenario scenario : scenarios) {
            scenario.register();
            scenario.activate();
        }
    }

    protected void unregisterAndDeactivateAllScenario() {
        for (Scenario scenario : scenarios) {
            scenario.deactivate();
            scenario.unregister();
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

    public abstract Set<UHCPlayer> getDeadAndAlivePlayers();

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

    public Set<UHCPlayer> getDeadPlayers() {
        return deadPlayers;
    }

    public Set<UHCPlayer> getSpectators() {
        return spectators;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public PvP getPvP() {
        return PvP;
    }
}
