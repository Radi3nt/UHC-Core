package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class RodLess extends Scenario {

    public RodLess(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("RodLess").setItemStack(new ItemStack(Material.FISHING_ROD)).setDescription("No fishing rod");
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        if (inv.getResult() == null)
            return;

        if (inv.getResult().getType().equals(Material.FISHING_ROD)) {
            inv.setResult(new ItemStack(Material.AIR));
        }
    }
}
