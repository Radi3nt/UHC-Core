package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HastyBoys extends Scenario {

    private final List<Material> enchantable = new ArrayList<>();
    private int digSpeedLevel = 3;
    private int durability = 2;

    public HastyBoys(UHCGame game) {
        super(game);
        enchantable.add(Material.DIAMOND_HOE);
        enchantable.add(Material.WOOD_AXE);
        enchantable.add(Material.WOOD_PICKAXE);
        enchantable.add(Material.WOOD_SPADE);
        enchantable.add(Material.GOLD_AXE);
        enchantable.add(Material.GOLD_PICKAXE);
        enchantable.add(Material.GOLD_SPADE);
        enchantable.add(Material.STONE_AXE);
        enchantable.add(Material.STONE_PICKAXE);
        enchantable.add(Material.STONE_SPADE);
        enchantable.add(Material.IRON_AXE);
        enchantable.add(Material.IRON_PICKAXE);
        enchantable.add(Material.IRON_SPADE);
        enchantable.add(Material.DIAMOND_AXE);
        enchantable.add(Material.DIAMOND_PICKAXE);
        enchantable.add(Material.DIAMOND_SPADE);
    }

    public static String getName() {
        return "HastyBoys";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (event.getInventory().getResult() == null) {
            return;
        }
        final Material itemType = event.getInventory().getResult().getType();
        if (!enchantable.contains(itemType))
            return;

        final ItemStack item = new ItemStack(itemType);
        item.addUnsafeEnchantment(Enchantment.DIG_SPEED, digSpeedLevel);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, durability);
        event.getInventory().setResult(item);
    }

    @ScenarioGetter(name = "Dig speed level")
    public int getDigSpeedLevel() {
        return digSpeedLevel;
    }

    @ScenarioSetter(name = "Dig speed level")
    public void setDigSpeedLevel(int digSpeedLevel) {
        this.digSpeedLevel = digSpeedLevel;
    }

    @ScenarioGetter(name = "Durability level")
    public int getDurability() {
        return durability;
    }

    @ScenarioSetter(name = "Durability level")
    public void setDurability(int durability) {
        this.durability = durability;
    }
}
