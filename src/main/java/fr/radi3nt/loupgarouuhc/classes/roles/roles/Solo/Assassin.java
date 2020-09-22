package fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin extends Role {

    public Assassin(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.ASSASSIN;
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999999, 0, true, false), true);
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

    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        ItemStack power3 = new ItemStack(Material.ENCHANTED_BOOK);
        power3.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
        ItemStack sharpness3 = new ItemStack(Material.ENCHANTED_BOOK);
        sharpness3.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        ItemStack prot3 = new ItemStack(Material.ENCHANTED_BOOK);
        prot3.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);


        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), power3);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), sharpness3);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), prot3);
    }
}
