package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

public class VanillaPlus extends Scenario {

    private float appleDropChance = 10f;
    private float flintDropChance = 20f;

    public VanillaPlus(UHCGame game) {
        super(game);
    }

    public static String getName() {
        return "Vanilla+";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.APPLE);
    }

    @EventHandler
    public void event(BlockBreakEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.isInGame() && !player.getGameData().isDead()) {

            if (player.getGameData().getGame() == game) {
                if (isActive()) {
                    if (e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2) {
                        SecureRandom random = new SecureRandom();
                        if (random.nextInt(100) < appleDropChance) {
                            for (ItemStack item : e.getBlock().getDrops()) {
                                if (item.getType() == Material.APPLE) {
                                    item.setType(Material.AIR);
                                }
                            }
                            player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE));
                        }
                    }

                    if (e.getBlock().getType() == Material.GRAVEL) {
                        SecureRandom random = new SecureRandom();
                        if (random.nextInt(100) < flintDropChance) {
                            e.setDropItems(false);
                            player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(Material.FLINT));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void event(LeavesDecayEvent e) {
        if (isActive()) {
            if (e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2) {
                SecureRandom random = new SecureRandom();
                if (random.nextInt(100) < appleDropChance) {
                    for (ItemStack item : e.getBlock().getDrops()) {
                        if (item.getType() == Material.APPLE) {
                            item.setType(Material.AIR);
                        }
                    }
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE));
                }
            }
        }
    }

    @ScenarioGetter(name = "Apple drop")
    public float getAppleDropChance() {
        return appleDropChance;
    }

    @ScenarioSetter(name = "Apple drop")
    public void setAppleDropChance(float appleDropChance) {
        this.appleDropChance = appleDropChance;
    }


    @ScenarioGetter(name = "Flint drop")
    public float getFlintDropChance() {
        return flintDropChance;
    }

    @ScenarioSetter(name = "Flint drop")
    public void setFlintDropChance(float flintDropChance) {
        this.flintDropChance = flintDropChance;
    }
}
