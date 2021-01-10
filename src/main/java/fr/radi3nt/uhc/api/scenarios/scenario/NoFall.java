package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoFall extends Scenario {

    public NoFall(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("NoFall").setItemStack(new ItemStack(Material.CHAINMAIL_BOOTS)).setDescription("Cancel fall damage");
    }

    @Override
    public void register() {
        super.register();
    }

    @EventHandler
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
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
