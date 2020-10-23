package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class DeadlyWater extends Scenario {

    private int damage = 20;

    public DeadlyWater(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "DeadlyWater";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @ScenarioEvent
    public void event(PlayerMoveEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getTo().getBlock().getType() == Material.WATER || e.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
                    player.getPlayer().damage(damage);
                }
            }
        }
    }

    @ScenarioGetter(name = "Damage value")
    public int getDamage() {
        return damage;
    }

    @ScenarioSetter(name = "Damage value")
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
