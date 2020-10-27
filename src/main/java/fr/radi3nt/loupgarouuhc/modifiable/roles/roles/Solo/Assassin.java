package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Solo;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin extends Role {

    public Assassin(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Assassin", WinType.SOLO, RoleType.NEUTRAL);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
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
        EnchantmentStorageMeta power3M = (EnchantmentStorageMeta) power3.getItemMeta();
        power3M.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        power3.setItemMeta(power3M);

        ItemStack sharpness3 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta sharpness3M = (EnchantmentStorageMeta) sharpness3.getItemMeta();
        sharpness3M.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);
        sharpness3.setItemMeta(sharpness3M);

        ItemStack prot3 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta prot3M = (EnchantmentStorageMeta) prot3.getItemMeta();
        prot3M.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        prot3.setItemMeta(prot3M);

        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), power3);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), sharpness3);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), prot3);
    }
}
