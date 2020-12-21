package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;

public class UHC extends Scenario {

    public UHC(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("UHC").setItemStack(new ItemStack(Material.CAKE)).setDescription("Prevent players from regaining health &4-NOT WORKING-");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game)
                if (isActive())
                    if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM) {
                        e.setCancelled(true);
                        e.setAmount(0);
                    }
        }
    }


}
