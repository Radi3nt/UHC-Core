package fr.radi3nt.uhc.api.game;

import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;

public interface UHCGame {

    void join(UHCPlayer player);

    boolean updateStart();

    void start();

    void end(List<UHCPlayer> winners, boolean fast);

    void kill(UHCPlayer player, Reason reason, Location playerloc);

    void scatter(UHCPlayer player);

    Location getGameSpawn();

    Set<UHCPlayer> getDeadAndAlivePlayers();

    GameState getState();

    GameData getData();

    GameTimer getGameTimer();

    Parameter getParameters();

    int getRadius();

    void setRadius(int radius);

    Set<UHCPlayer> getAlivePlayers();

    Set<UHCPlayer> getDeadPlayers();

    Set<UHCPlayer> getSpectators();

    List<Scenario> getScenarios();

    UHCPlayer getUHCPlayerInThisGame(String name);

    PvP getPvP();
}
