package fr.radi3nt.loupgarouuhc.roles.roles;

import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class VillagerRoleIdentity {

    private final RoleIdentity identity;

    public VillagerRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionPermanent, List<PotionEffect> potionEffectsDay, List<PotionEffect> potionEffectsNight, int maxHealth) {
        identity = new RoleIdentity(name, WinType.VILLAGE, RoleType.VILLAGER);
        identity.setRoleItems(roleItems);
        identity.setPotionEffectsDay(potionEffectsDay);
        identity.setPotionPermanant(potionPermanent);
        identity.setPotionEffectsNight(potionEffectsNight);
        identity.setMaxHealth(maxHealth);
    }

    public VillagerRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionEffectsDay, List<PotionEffect> potionEffectsNight, int maxHealth) {
        identity = new RoleIdentity(name, WinType.VILLAGE, RoleType.VILLAGER);
        identity.setRoleItems(roleItems);
        identity.setPotionEffectsDay(potionEffectsDay);
        identity.setPotionEffectsNight(potionEffectsNight);
        identity.setMaxHealth(maxHealth);
    }

    public VillagerRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionPermanent, int maxHealth) {
        identity = new RoleIdentity(name, WinType.VILLAGE, RoleType.VILLAGER);
        identity.setRoleItems(roleItems);
        identity.setPotionPermanant(potionPermanent);
        identity.setMaxHealth(maxHealth);
    }

    public VillagerRoleIdentity(String name, List<ItemStack> roleItems, int maxHealth) {
        identity = new RoleIdentity(name, WinType.VILLAGE, RoleType.VILLAGER);
        identity.setRoleItems(roleItems);
        identity.setMaxHealth(maxHealth);
    }

    public VillagerRoleIdentity(String name, int maxHealth) {
        identity = new RoleIdentity(name, WinType.VILLAGE, RoleType.VILLAGER);
        identity.setMaxHealth(maxHealth);
    }


    public RoleIdentity getIdentity() {
        return identity;
    }
}
