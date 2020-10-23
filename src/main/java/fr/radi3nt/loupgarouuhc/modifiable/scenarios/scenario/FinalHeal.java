package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioCommand;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;

public class FinalHeal extends Scenario {

    private int time = 20;

    public FinalHeal(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "FinalHeal";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.GOLDEN_APPLE);
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                if (tick == time * 20 * 60) {
                    doFinalHeal();
                }
            }
        }
    }

    private void doFinalHeal() {
        for (LGPlayer lgPlayer : game.getGamePlayers()) {
            if (lgPlayer.getPlayer() != null) {
                lgPlayer.getPlayer().setHealth(lgPlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }
        }
        Bukkit.broadcastMessage(prefix + " " + ChatColor.GOLD + "Final heal >" + ChatColor.AQUA + " All players have been healed");
    }

    @ScenarioCommand
    public void onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (isActive()) {
            fr.radi3nt.loupgarouuhc.commands.Command command1 = new fr.radi3nt.loupgarouuhc.commands.Command(commandSender, command, s, strings);
            if (command1.executeCommand("uhc.fh.activate", "uhc.finalheal.activate", 0, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                doFinalHeal();
            }
        }
    }

    @ScenarioGetter(name = "Time")
    public int getTime() {
        return time;
    }

    @ScenarioSetter(name = "Time")
    public void setTime(int time) {
        this.time = time;
    }
}
