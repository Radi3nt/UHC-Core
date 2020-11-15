package fr.radi3nt.loupgarouuhc.roles.roles.Solo;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.SoloRoleIdentity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Assassin extends Role {

    public Assassin(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<ItemStack> rolesItems = new ArrayList<>();

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

        rolesItems.add(power3);
        rolesItems.add(sharpness3);
        rolesItems.add(prot3);

        ArrayList<PotionEffect> dayPotionEffect = new ArrayList<>();
        dayPotionEffect.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999 * 20, 0, true, false, Color.AQUA));

        return new SoloRoleIdentity("Assassin", rolesItems, dayPotionEffect, new ArrayList<>(), 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void night(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void day(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {

    }

    @Override
    public void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }

    @Override
    public void discoverRole(LGGame game, UHCPlayer lgp) {

    }
}
