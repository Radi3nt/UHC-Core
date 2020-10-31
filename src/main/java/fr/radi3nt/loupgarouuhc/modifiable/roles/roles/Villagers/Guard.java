package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.VillagerRoleIdentity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.security.SecureRandom;
import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefixPrivé;

public class Guard extends Role {

    public Guard(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<ItemStack> rolesItems = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        itemStack.addEnchantment(Enchantment.SWEEPING_EDGE, 3);
        itemStack.setDurability((short) (new SecureRandom().nextInt(100) + 100));
        itemStack.getItemMeta().setDisplayName("Guard's sword");
        rolesItems.add(itemStack);
        return new VillagerRoleIdentity("Guard", rolesItems, 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void night(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void day(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void killSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void killed(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {
        if (killer != null && killer.getGameData().getGame() == killed.getGameData().getGame()) {
            killer.getPlayer().damage(2);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 30, 1, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60 * 30, 0, true), true);
            killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 30, 1, true), true);
            killed.getGameData().getKiller().sendMessage(getPrefix() + " " + getPrefixPrivé() + ChatColor.GOLD + " Tu as été infecté par le garde");
        }
    }

    @Override
    public void discoverRole(LGGame game, LGPlayer lgp) {

    }

}
