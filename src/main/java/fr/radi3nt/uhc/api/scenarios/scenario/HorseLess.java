package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

public class HorseLess extends Scenario {

    public HorseLess(UHCGame game) {
        super(game);
    }

    public static String getName() {
        return "HorseLess";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.SADDLE);
    }

    @EventHandler
    public void event(PlayerInteractAtEntityEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game)
            if (isActive())
                if (e.getRightClicked().getType() == EntityType.HORSE)
                    e.setCancelled(true);
    }

    @EventHandler
    public void event(EntityMountEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
            if (player.getGameData().getGame() == game)
                if (isActive())
                    if (e.getMount().getType() == EntityType.HORSE)
                        e.setCancelled(true);
        }
    }


}
