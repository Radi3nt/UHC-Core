package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class FastFurnace extends Scenario {

    private final Set<Location> locations = new HashSet<>();
    private int increase = 3;
    private boolean changeBurn = true;

    public FastFurnace(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "FastFurnace";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.FURNACE);
    }

    private void startUpdate(Location block) {
        if (locations.contains(block)) {
            return;
        }
        locations.add(block);
        new BukkitRunnable() {
            public void run() {
                if (block.getBlock().getType() == Material.FURNACE || block.getBlock().getType() == Material.BURNING_FURNACE) {
                    Furnace state = (Furnace) block.getBlock().getState();
                    if (state.getCookTime() < 200 && state.getBurnTime() > 0) {
                        if (state.getInventory().getSmelting() != null) {
                            state.setCookTime((short) (state.getCookTime() + increase));
                            if (changeBurn)
                                state.setBurnTime((short) (state.getBurnTime() - increase));
                            state.update();
                            if (state.getCookTime() == 200) {
                                locations.remove(block);
                                this.cancel();
                            }
                        }
                    } else {
                        locations.remove(block);
                        this.cancel();
                    }
                } else {
                    locations.remove(block);
                    this.cancel();
                }
            }
        }.runTaskTimer(LoupGarouUHC.getPlugin(), 1L, 1L);
    }

    @ScenarioEvent
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        startUpdate(event.getBlock().getLocation());
    }

    @ScenarioEvent
    public void onFurnaceBurn(FurnaceSmeltEvent event) {
        startUpdate(event.getBlock().getLocation());
    }

    @ScenarioGetter(name = "Value")
    public int getIncrease() {
        return increase;
    }

    @ScenarioSetter(name = "Value")
    public void setIncrease(int increase) {
        this.increase = increase;
    }

    @ScenarioGetter(name = "Burn time")
    public boolean getChangeBurn() {
        return changeBurn;
    }

    @ScenarioSetter(name = "Burn time")
    public void setIncrease(boolean changeBurn) {
        this.changeBurn = changeBurn;
    }
}
