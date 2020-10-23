package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class RodLess extends Scenario {

    public RodLess(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "RodLess";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.FISHING_ROD);
    }

    @ScenarioEvent
    public void onCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        if (inv.getResult() == null)
            return;

        if (inv.getResult().getType().equals(Material.FISHING_ROD)) {
            inv.setResult(new ItemStack(Material.AIR));
        }
    }
}
