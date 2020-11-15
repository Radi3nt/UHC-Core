package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoDrown extends Scenario {

    public NoDrown(UHCGame game) {
        super(game);
    }

    public static String getName() {
        return "NoDrown";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.WATER_LILY);
    }

    @EventHandler
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
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
