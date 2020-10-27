package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoDrown extends Scenario {

    public NoDrown(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "NoDrown";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.WATER_LILY);
    }

    @ScenarioEvent
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            LGPlayer player = LGPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game) {
                if (isActive()) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                        e.setCancelled(true);
                        e.setDamage(0);
                    }
                }
            }
        }
    }
}
