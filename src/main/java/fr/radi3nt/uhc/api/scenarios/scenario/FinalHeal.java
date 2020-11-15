package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioCommand;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

public class FinalHeal extends Scenario {

    private int time = 20;

    public FinalHeal(UHCGame game) {
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
        super.tick(gameTimer, tick);
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                if (tick == time * 20 * 60) {
                    doFinalHeal();
                }
            }
        }
    }

    private void doFinalHeal() {
        for (UHCPlayer lgPlayer : game.getAlivePlayers()) {
            lgPlayer.getPlayerStats().refresh();
            lgPlayer.getPlayerStats().setHealth(lgPlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            lgPlayer.getPlayerStats().update();
        }
        UHCCore.broadcastMessage(UHCCore.getPrefix() + " " + ChatColor.GOLD + "Final heal >" + ChatColor.AQUA + " All players have been healed");
    }

    @ScenarioCommand
    public void onCommand(CommandUtilis command) {
        if (isActive()) {
            try {
                if (command.executeCommand("uhc.fh.activate", "uhc.finalheal.activate", 0, CommandUtilis.Checks.GAME)) {
                    doFinalHeal();
                }
            } catch (NoPermissionException e) {

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
