package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioCommand;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PvP extends Scenario {

    protected final List<Integer> timeAlert;
    private int time;
    private boolean timerActivated = true;
    private boolean pvp = false;

    public PvP(LGGame game, int time, List<Integer> timeAlert) {
        super(game);
        this.time = time;
        this.timeAlert = timeAlert;
    }

    public static String getName() {
        return "PvP";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                if (timerActivated) {
                    for (Integer integer : timeAlert) {
                        if (tick == integer) {
                            Chat.broadcastIdMessage("gameTimerPvpSoon", game.getGamePlayersWithDeads());
                        }
                    }
                    if (tick == time) {
                        activatePvp();
                    }
                }
            }
        }
    }

    public void activatePvp() {
        pvp = true;
        Chat.broadcastIdMessage("gameTimerPvpOn", game.getGamePlayersWithDeads());
    }

    public void deactivatePvp() {
        pvp = false;
        Chat.broadcastIdMessage("gameTimerPvpOff", game.getGamePlayersWithDeads());
    }

    @ScenarioCommand
    public void onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (isActive()) {
            fr.radi3nt.loupgarouuhc.commands.Command command1 = new fr.radi3nt.loupgarouuhc.commands.Command(commandSender, command, s, strings);
            String[] activate = new String[3];
            String base = "uhc.pvp.";
            activate[0] = base + "activate";
            activate[1] = base + "active";
            activate[2] = base + "on";
            String[] deactivate = new String[2];
            deactivate[0] = base + "deactivate";
            deactivate[1] = base + "off";
            if (command1.executeCommand(activate, "uhc.pvp.activate", 0, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                activatePvp();
            } else if (command1.executeCommand(deactivate, "uhc.pvp.deactivate", 0, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                deactivatePvp();
            } else if (command1.executeCommand(base + "timer.off", "uhc.pvp.timer", 0, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                timerActivated = false;
            } else if (command1.executeCommand(base + "timer.on", "uhc.pvp.timer", 0, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                timerActivated = true;
            }
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Integer> getTimeAlert() {
        return timeAlert;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isTimerActivated() {
        return timerActivated;
    }

    public void setTimerActivated(boolean timerActivated) {
        this.timerActivated = timerActivated;
    }
}
