package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class XPBoost extends Scenario {

    private final int xpBoostPercentage;
    private final Map<Material, Integer> xpMaterial;

    public XPBoost(LGGame game, int xpBoostPercentage, Map<Material, Integer> xpMaterial) {
        super(game);
        this.xpBoostPercentage = xpBoostPercentage;
        this.xpMaterial = xpMaterial;
    }

    public static String getName() {
        return "XpBoost";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BEACON);
    }


    @ScenarioEvent
    public void event(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive() && !e.isCancelled()) {
                if (xpMaterial.containsKey(e.getBlock().getType())) {
                    (e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), ExperienceOrb.class)).setExperience(xpMaterial.get(e.getBlock().getType()));
                }
            }
        }
    }

    @ScenarioEvent
    public void event(PlayerExpChangeEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getAmount() > 0) {
                    e.getPlayer().giveExp((int) ((((float) xpBoostPercentage / 100) * 17) + (float) e.getAmount()));
                    e.setAmount(0);
                }
            }
        }
    }

}
