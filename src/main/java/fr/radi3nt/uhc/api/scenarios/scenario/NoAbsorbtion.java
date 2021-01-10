package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class NoAbsorbtion extends Scenario {

    public NoAbsorbtion(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("NoAbsorbtion").setItemStack(new ItemStack(Material.GOLDEN_APPLE)).setDescription("Cancel golden apple absorbtion");
    }

    @EventHandler
    public void event(PlayerItemConsumeEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game)
            if (isActive())
                if (e.getItem().getType() == Material.GOLDEN_APPLE)
                    Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), () -> e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION));
    }

}
