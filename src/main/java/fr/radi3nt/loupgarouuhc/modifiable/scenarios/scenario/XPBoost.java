package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class XPBoost extends Scenario {

    protected int xpBoostPercentage = 50;
    protected Map<Material, Integer> xpMaterial = new HashMap<>();

    public XPBoost(LGGame game) {
        super(game);
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

    @ScenarioGetter(name = "XP Boost")
    public int getXpBoostPercentage() {
        return xpBoostPercentage;
    }

    @ScenarioSetter(name = "XP Boost")
    public void setXpBoostPercentage(int xpBoostPercentage) {
        this.xpBoostPercentage = xpBoostPercentage;
    }

    @ScenarioGetter(name = "XP Materials")
    public Map<Material, Integer> getXpMaterial() {
        return xpMaterial;
    }

    @ScenarioSetter(name = "XP Materials")
    public void setXpMaterial(Map<Material, Integer> xpMaterial) {
        this.xpMaterial = xpMaterial;
    }
}
