package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FastFurnace extends Scenario {

    private int increase;

    public FastFurnace(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "FastFurnace";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.FURNACE);
    }

    private void startUpdate(Block block) {
        new BukkitRunnable() {
            public void run() {
                if (block.getType() != Material.FURNACE || block.getType() != Material.BURNING_FURNACE) {
                    Furnace state = (Furnace) block.getState();
                    if (state.getCookTime() > 0 || state.getBurnTime() > 0) {
                        state.setCookTime((short) (state.getCookTime() + increase));
                        state.update();
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(LoupGarouUHC.getPlugin(), 1L, 1L);
    }

    @ScenarioEvent
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        startUpdate(event.getBlock());
    }

    @ScenarioGetter(name = "Value")
    public int getIncrease() {
        return increase;
    }

    @ScenarioSetter(name = "Value")
    public void setIncrease(int increase) {
        this.increase = increase;
    }
}
