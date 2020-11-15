package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class DeadlyWater extends Scenario {

    private int damage = 20;

    public DeadlyWater(UHCGame game) {
        super(game);
    }

    public static String getName() {
        return "DeadlyWater";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @EventHandler
    public void event(PlayerMoveEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getTo().getBlock().getType() == Material.WATER || e.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
                    player.getPlayer().damage(damage);
                }
            }
        }
    }

    @ScenarioGetter(name = "Damage value")
    public int getDamage() {
        return damage;
    }

    @ScenarioSetter(name = "Damage value")
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
