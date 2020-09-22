package fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefixPrivé;

public class Guard extends Role {

    public Guard(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.GUARD;
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
    }

    @Override
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {
        if (killer!=null && killer.getGame()==killed.getGame()) {
            killer.getPlayer().damage(2);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60*30, 0, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*30, 1, true), true);
            killed.getKiller().sendMessage(prefix + " " + prefixPrivé + ChatColor.GOLD + " Tu as été infecté par le garde");
        }
    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        itemStack.addEnchantment(Enchantment.SWEEPING_EDGE, 3);
        itemStack.setDurability((short) 128);
        itemStack.getItemMeta().setDisplayName("Guard's sword");
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), itemStack);
    }

    public static ArrayList<Location> generateSphere(Location center, int radius) {
        ArrayList<Location> circlesBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();

        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX -x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
    }

}
