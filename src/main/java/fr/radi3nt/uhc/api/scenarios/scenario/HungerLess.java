package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class HungerLess extends Scenario {

    private float value = 0;

    public HungerLess(UHCGame game) {
        super(game);
    }

    public static String getName() {
        return "HungerLess";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @EventHandler
    public void event(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
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
