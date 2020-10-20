package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CutClean extends Scenario {

    public CutClean(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "CutClean";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.IRON_INGOT);
    }

    @Override
    public void register() {
        super.register();
    }

    @ScenarioEvent
    public void event(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getBlock().getType() == Material.IRON_ORE) {
                    e.setDropItems(false);
                    player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(Material.IRON_INGOT));
                }
                if (e.getBlock().getType() == Material.GOLD_ORE) {
                    e.setDropItems(false);
                    player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLD_INGOT));
                }
            }
        }
    }

}
