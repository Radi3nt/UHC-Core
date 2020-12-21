package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.events.UHCGameStartsEvent;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Meetup extends Scenario {

    private List<ItemStack> itemStacks = new ArrayList<>();

    public Meetup(UHCGame game) {
        super(game);

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.DURABILITY, 1);
        ItemStack blocks1 = new ItemStack(Material.LOG, 64);
        ItemStack blocks2 = new ItemStack(Material.LOG, 64);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        ItemStack arrows = new ItemStack(Material.ARROW, 32);

        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 6);
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 2);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 1);

        ItemStack sheers = new ItemStack(Material.SHEARS, 1);
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 10);
        ItemStack cobble1 = new ItemStack(Material.COBBLESTONE, 64);
        ItemStack cobble2 = new ItemStack(Material.COBBLESTONE, 64);


        ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemStack water = new ItemStack(Material.WATER_BUCKET, 2);
        ItemStack leather = new ItemStack(Material.LEATHER, 3);
        ItemStack can = new ItemStack(Material.SUGAR_CANE, 2);

        itemStacks.add(sword);
        itemStacks.add(blocks1);
        itemStacks.add(blocks2);
        itemStacks.add(cobble1);
        itemStacks.add(cobble2);
        itemStacks.add(helmet);
        itemStacks.add(chestplate);
        itemStacks.add(leggings);
        itemStacks.add(boots);
        itemStacks.add(bow);
        itemStacks.add(arrows);
        itemStacks.add(gapple);
        itemStacks.add(pickaxe);
        itemStacks.add(sheers);
        itemStacks.add(iron);
        itemStacks.add(lava);
        itemStacks.add(water);
        itemStacks.add(leather);
        itemStacks.add(can);
    }

    public static String getName() {
        return "Meetup";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_CHESTPLATE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGameStart(UHCGameStartsEvent e) {
        for (UHCPlayer alivePlayer : e.getGame().getAlivePlayers()) {
            alivePlayer.getPlayerStats().refresh();
            for (ItemStack itemStack : itemStacks) {
                if (Arrays.asList(alivePlayer.getPlayerStats().getInventory().getContents()).contains(null))
                    alivePlayer.getPlayerStats().getInventory().addItem(itemStack);
                else
                    alivePlayer.getPlayerStats().getLastLocation().getWorld().dropItem(alivePlayer.getPlayerStats().getLastLocation(), itemStack);
            }
            alivePlayer.getPlayerStats().update();
        }
    }
}
