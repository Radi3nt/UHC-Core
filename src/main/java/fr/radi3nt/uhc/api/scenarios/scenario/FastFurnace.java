package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
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

    public FastFurnace(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("FastFurnace").setItemStack(new ItemStack(Material.FURNACE)).setDescription("Change general cook time");
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
        }.runTaskTimer(UHCCore.getPlugin(), 1L, 1L);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        startUpdate(event.getBlock().getLocation());
    }

    @EventHandler
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
