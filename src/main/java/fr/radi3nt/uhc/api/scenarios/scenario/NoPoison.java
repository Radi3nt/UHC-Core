package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoPoison extends Scenario {

    public NoPoison(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("NoPoison").setItemStack(new ItemStack(Material.POISONOUS_POTATO)).setDescription("Cancel poison damage");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }
}
