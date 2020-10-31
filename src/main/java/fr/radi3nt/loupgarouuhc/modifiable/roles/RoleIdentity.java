package fr.radi3nt.loupgarouuhc.modifiable.roles;

import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.RoleLang;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoleIdentity {

    private final String id;
    private final WinType winType;
    private final RoleType roleType;

    private List<ItemStack> roleItems = new ArrayList<>();
    private List<PotionEffect> potionPermanant = new ArrayList<>();
    private List<PotionEffect> potionEffectsDay = new ArrayList<>();
    private List<PotionEffect> potionEffectsNight = new ArrayList<>();
    private int maxHealth = 20;

    public RoleIdentity(String id, WinType winType, RoleType roleType) {
        this.id = id;
        this.winType = winType;
        this.roleType = roleType;
    }

    public WinType getWinType() {
        return winType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public String getId() {
        return id;
    }

    public String getName(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getName();
            }
        }
        return language.getDefaultMessage();
    }

    public String getShortDescription(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getShortDesc();
            }
        }
        return language.getDefaultMessage();
    }

    public String getRoleDescription(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getDesc();
            }
        }
        return language.getDefaultMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleIdentity that = (RoleIdentity) o;
        return id.equals(that.id) &&
                winType == that.winType &&
                roleType == that.roleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, winType, roleType);
    }

    public List<PotionEffect> getPotionEffectsNight() {
        return potionEffectsNight;
    }

    public void setPotionEffectsNight(List<PotionEffect> potionEffectsNight) {
        this.potionEffectsNight = potionEffectsNight;
    }

    public List<PotionEffect> getPotionEffectsDay() {
        return potionEffectsDay;
    }

    public void setPotionEffectsDay(List<PotionEffect> potionEffectsDay) {
        this.potionEffectsDay = potionEffectsDay;
    }

    public List<ItemStack> getRoleItems() {
        return roleItems;
    }

    public void setRoleItems(List<ItemStack> roleItems) {
        this.roleItems = roleItems;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public List<PotionEffect> getPotionPermanant() {
        return potionPermanant;
    }

    public void setPotionPermanant(List<PotionEffect> potionPermanant) {
        this.potionPermanant = potionPermanant;
    }
}
