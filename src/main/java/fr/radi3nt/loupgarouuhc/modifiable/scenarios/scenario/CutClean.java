package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CutClean extends Scenario {

    static HashMap<Material, Material> materials = new HashMap<>();

    static {
        materials.put(Material.IRON_ORE, Material.IRON_INGOT);
        materials.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        materials.put(Material.RAW_BEEF, Material.COOKED_BEEF);
        materials.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
        materials.put(Material.MUTTON, Material.COOKED_MUTTON);
        materials.put(Material.RABBIT, Material.COOKED_RABBIT);
        materials.put(Material.PORK, Material.GRILLED_PORK);
    }

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
                if (materials.containsKey(e.getBlock().getType())) {
                    e.setDropItems(false);
                    player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(materials.get(e.getBlock().getType())));
                }
            }
        }
    }

    @ScenarioEvent
    public void event(EntityDeathEvent e) {
        if (isActive()) {
            for (ItemStack drop : e.getDrops()) {
                if (materials.containsKey(drop.getType())) {
                    e.getDrops().clear();
                    e.getEntity().getWorld().dropItem(e.getEntity().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(materials.get(drop.getType())));
                    break;
                }
            }
        }
    }
}
