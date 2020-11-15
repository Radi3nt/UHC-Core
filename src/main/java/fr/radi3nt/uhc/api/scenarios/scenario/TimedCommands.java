package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.events.UHCGameEndsEvent;
import fr.radi3nt.uhc.api.events.UHCGameStartsEvent;
import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TimedCommands extends Scenario {

    protected List<String> startCommands = new ArrayList<>();
    protected List<String> deadCommands = new ArrayList<>();
    protected List<String> endCommands = new ArrayList<>();

    public TimedCommands(UHCGame game) {
        super(game);
        startCommands.add("toggleeditwand");
        startCommands.add("pa leave");
        endCommands.add("toggleeditwand");
    }

    public static String getName() {
        return "TimedCommands";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BOOK);
    }


    @EventHandler
    public void event(UHCGameStartsEvent e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (UHCPlayer gamePlayer : e.getGame().getDeadAndAlivePlayers()) {
                    for (String item : startCommands) {
                        gamePlayer.getPlayer().performCommand(item);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(UHCPlayerKilledEvent e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (String item : deadCommands) {
                    e.getKilled().getPlayer().performCommand(item);
                }
            }
        }
    }

    @EventHandler
    public void event(UHCGameEndsEvent e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (UHCPlayer gamePlayer : e.getGame().getDeadAndAlivePlayers()) {
                    for (String item : endCommands) {
                        gamePlayer.getPlayer().performCommand(item);
                    }
                }
            }
        }
    }

    //todo annotations
    public List<String> getStartCommands() {
        return startCommands;
    }

    public void setStartCommands(List<String> startCommands) {
        this.startCommands = startCommands;
    }

    public List<String> getDeadCommands() {
        return deadCommands;
    }

    public void setDeadCommands(List<String> deadCommands) {
        this.deadCommands = deadCommands;
    }

    public List<String> getEndCommands() {
        return endCommands;
    }

    public void setEndCommands(List<String> endCommands) {
        this.endCommands = endCommands;
    }
}
