package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FireLess extends Scenario {

    private float value = 100;

    public FireLess(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("FireLess").setItemStack(new ItemStack(Material.FLINT_AND_STEEL)).setDescription("Prevent from getting damaged by fire");
    }

    @EventHandler
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
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
