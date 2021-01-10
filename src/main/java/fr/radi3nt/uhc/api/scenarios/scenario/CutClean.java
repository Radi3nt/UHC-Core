package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class CutClean extends Scenario {

    private final HashMap<Material, Material> materials = new HashMap<>();

    public CutClean(UHCGame game) {
        super(game);
        materials.put(Material.IRON_ORE, Material.IRON_INGOT);
        materials.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        materials.put(Material.RAW_BEEF, Material.COOKED_BEEF);
        materials.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
        materials.put(Material.MUTTON, Material.COOKED_MUTTON);
        materials.put(Material.RABBIT, Material.COOKED_RABBIT);
        materials.put(Material.PORK, Material.GRILLED_PORK);
    }

    public static ScenarioData getData() {
        return new ScenarioData("CutClean").setItemStack(new ItemStack(Material.IRON_INGOT)).setDescription("Auto smelt items");
    }

    @Override
    public void register() {
        super.register();
    }

    @EventHandler
    public void event(BlockBreakEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                for (ItemStack drop : e.getBlock().getDrops(e.getPlayer().getItemInHand())) {
                    if (materials.containsKey(drop.getType())) {
                        e.setDropItems(false);
                        player.getPlayer().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(materials.get(e.getBlock().getType()), drop.getAmount()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void event(EntityDeathEvent e) {
        if (isActive()) {
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack drop : e.getDrops()) {
                if (materials.containsKey(drop.getType())) {
                    itemStacks.add(drop);
                    e.getEntity().getWorld().dropItem(e.getEntity().getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(materials.get(drop.getType()), drop.getAmount()));
                }
            }
            for (ItemStack itemStack : itemStacks) {
                e.getDrops().remove(itemStack);
            }
        }
    }
}
