package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoFall extends Scenario {

    public NoFall(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "NoFall";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.CHAINMAIL_BOOTS);
    }

    @Override
    public void register() {
        super.register();
    }

    @ScenarioEvent
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            LGPlayer player = LGPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game) {
                if (isActive()) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        e.setCancelled(true);
                        e.setDamage(0.0);
                    }
                }
            }
        }
    }

}
