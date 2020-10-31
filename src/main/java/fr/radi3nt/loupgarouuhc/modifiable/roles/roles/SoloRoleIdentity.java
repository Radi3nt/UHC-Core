package fr.radi3nt.loupgarouuhc.modifiable.roles.roles;

import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class SoloRoleIdentity {

    private final RoleIdentity identity;

    public SoloRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionPermanent, List<PotionEffect> potionEffectsDay, List<PotionEffect> potionEffectsNight, int maxHealth) {
        identity = new RoleIdentity(name, WinType.SOLO, RoleType.NEUTRAL);
        identity.setRoleItems(roleItems);
        identity.setPotionEffectsDay(potionEffectsDay);
        identity.setPotionPermanant(potionPermanent);
        identity.setPotionEffectsNight(potionEffectsNight);
        identity.setMaxHealth(maxHealth);
    }

    public SoloRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionEffectsDay, List<PotionEffect> potionEffectsNight, int maxHealth) {
        identity = new RoleIdentity(name, WinType.SOLO, RoleType.NEUTRAL);
        identity.setRoleItems(roleItems);
        identity.setPotionEffectsDay(potionEffectsDay);
        identity.setPotionEffectsNight(potionEffectsNight);
        identity.setMaxHealth(maxHealth);
    }

    public SoloRoleIdentity(String name, List<ItemStack> roleItems, List<PotionEffect> potionPermanent, int maxHealth) {
        identity = new RoleIdentity(name, WinType.SOLO, RoleType.NEUTRAL);
        identity.setRoleItems(roleItems);
        identity.setPotionPermanant(potionPermanent);
        identity.setMaxHealth(maxHealth);
    }

    public SoloRoleIdentity(String name, List<ItemStack> roleItems, int maxHealth) {
        identity = new RoleIdentity(name, WinType.SOLO, RoleType.NEUTRAL);
        identity.setRoleItems(roleItems);
        identity.setMaxHealth(maxHealth);
    }

    public SoloRoleIdentity(String name, int maxHealth) {
        identity = new RoleIdentity(name, WinType.SOLO, RoleType.NEUTRAL);
        identity.setMaxHealth(maxHealth);
    }


    public RoleIdentity getIdentity() {
        return identity;
    }
}
