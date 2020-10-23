package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FireLess extends Scenario {

    private float value = 100;

    public FireLess(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "FireLess";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.LAVA_BUCKET);
    }

    @ScenarioEvent
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            LGPlayer player = LGPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game) {
                if (isActive()) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                        e.setDamage(((float) e.getDamage() - (value / 100) * e.getDamage()));
                    }
                }
            }
        }
    }

    @ScenarioGetter(name = "Damage")
    public float getValue() {
        return value;
    }

    @ScenarioSetter(name = "Damage")
    public void setValue(float value) {
        this.value = value;
    }
}
