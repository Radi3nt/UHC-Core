package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnEndGame;
import fr.radi3nt.loupgarouuhc.event.events.OnKill;
import fr.radi3nt.loupgarouuhc.event.events.OnStartGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TimedCommands extends Scenario {

    private final List<String> startCommands;
    private final List<String> deadCommands;
    private final List<String> endCommands;

    public TimedCommands(LGGame game, List<String> startCommands, List<String> deadCommands, List<String> endCommands) {
        super(game);
        this.startCommands = startCommands;
        this.endCommands = endCommands;
        this.deadCommands = deadCommands;
    }

    public static String getName() {
        return "TimedCommands";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BOOK);
    }


    @ScenarioEvent
    public void event(OnStartGame e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (LGPlayer gamePlayer : e.getGame().getGamePlayers()) {
                    for (String item : startCommands) {
                        gamePlayer.getPlayer().performCommand(item);
                    }
                }
            }
        }
    }

    @ScenarioEvent
    public void event(OnKill e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (LGPlayer gamePlayer : e.getGame().getGamePlayers()) {
                    for (String item : deadCommands) {
                        gamePlayer.getPlayer().performCommand(item);
                    }
                }
            }
        }
    }

    @ScenarioEvent
    public void event(OnEndGame e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (LGPlayer gamePlayer : e.getGame().getGamePlayers()) {
                    for (String item : endCommands) {
                        gamePlayer.getPlayer().performCommand(item);
                    }
                }
            }
        }
    }

}
