package fr.radi3nt.uhc.api.game;

import fr.radi3nt.uhc.api.game.instances.GameData;
import fr.radi3nt.uhc.api.game.reasons.Reason;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.scenario.PvP;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UHCGame {

    UUID getUUID();

    void join(UHCPlayer player);
    void forceJoin(UHCPlayer player);
    void spectate(UHCPlayer player);

    boolean updateStart();

    void updateWin();

    void start();

    void end(List<UHCPlayer> winners, boolean fast);

    void kill(UHCPlayer player, Reason reason, Location playerloc);

    void scatter(UHCPlayer player);

    GameState getState();

    GameData getData();

    GameTimer getGameTimer();

    Parameter getParameters();

    int getRadius();

    void setRadius(int radius);

    Set<UHCPlayer> getAlivePlayers();

    Set<UHCPlayer> getSpectators();

    Set<UHCPlayer> getSpectatorsAndAlivePlayers();

    Set<UHCPlayer> getWaitQueue();

    List<Scenario> getScenarios();

    void addScenario(Scenario scenario);

    void removeScenario(Scenario scenario);

    UHCPlayer getUHCPlayerInThisGame(String name);

    PvP getPvP();
}
