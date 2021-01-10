package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MeetupAlert extends Scenario {

    private final int meetupTime = 30;
    private final int timeBeforeSurfaceAlert = 5;

    public MeetupAlert(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("MeetupAlert").setItemStack(new ItemStack(Material.LEATHER_CHESTPLATE)).setDescription("Alerts player that they need to go to 0 0 and get out of the caves");
    } //todo sound

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        super.tick(gameTimer, tick);
        if (tick==meetupTime*60*20) {
            for (UHCPlayer alivePlayer : gameTimer.getGame().getAlivePlayers()) {
                alivePlayer.sendTitle(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "/!\\" + ChatColor.DARK_RED + "" + ChatColor.BOLD + " MEETUP " + ChatColor.GOLD + "" + ChatColor.UNDERLINE + "/!\\", ChatColor.GRAY + "You must go to " + ChatColor.WHITE + "0 0", 10, 3*20, 20);
            }
        }
        if (tick>=(meetupTime+timeBeforeSurfaceAlert)*60*20) {
            if ((tick-(meetupTime+timeBeforeSurfaceAlert)*60*20) % (20*20) == 0) {
                for (UHCPlayer alivePlayer : gameTimer.getGame().getAlivePlayers()) {
                    alivePlayer.getPlayerStats().refresh();
                    if (alivePlayer.getPlayerStats().getLastLocation().getY() < alivePlayer.getPlayerStats().getLastLocation().getWorld().getSeaLevel() - 5)
                        alivePlayer.sendTitle(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "/!\\" + ChatColor.DARK_RED + "" + ChatColor.BOLD + " MEETUP " + ChatColor.GOLD + "" + ChatColor.UNDERLINE + "/!\\", ChatColor.GRAY + "You must go to " + ChatColor.WHITE + "the surface", 10, 3 * 20, 20);
                }
            }
        }
    }
}
