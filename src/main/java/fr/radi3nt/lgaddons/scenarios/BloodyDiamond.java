package fr.radi3nt.lgaddons.scenarios;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BloodyDiamond extends Scenario {

    private double damage = 1;

    public BloodyDiamond(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "Bloody diamond";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND);
    }

    @ScenarioEvent
    public void playerBreakBlock(BlockBreakEvent e) {
        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        if (lgp.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getBlock().getType() == Material.DIAMOND_ORE) {
                    e.getPlayer().damage(damage);
                }
            }
        }
    }

    @ScenarioGetter(name = "Damage value")
    public double getDamage() {
        return damage;
    }

    @ScenarioSetter(name = "Damage value")
    public void setDamage(double damage) {
        this.damage = damage;
    }
}
