package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

public class VanillaPlus extends Scenario {

    protected float appleDropChance = 0.1f;
    protected float flintDropChance = 10;

    public VanillaPlus(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "Vanilla+";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.APPLE);
    }

    @ScenarioEvent
    public void event(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
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
