package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class HungerLess extends Scenario {

    private float value = 0;

    public HungerLess(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "HungerLess";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @ScenarioEvent
    public void event(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            LGPlayer player = LGPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game) {
                if (isActive()) {
                    if (((Player) e.getEntity()).getFoodLevel() > e.getFoodLevel())
                        e.setFoodLevel((int) (((Player) e.getEntity()).getFoodLevel() - (value / 100) * e.getFoodLevel()));
                }
            }
        }
    }

    @ScenarioGetter(name = "HungerLoss percentage")
    public float getValue() {
        return value;
    }

    @ScenarioSetter(name = "HungerLoss percentage")
    public void setValue(float value) {
        this.value = value;
    }
}
