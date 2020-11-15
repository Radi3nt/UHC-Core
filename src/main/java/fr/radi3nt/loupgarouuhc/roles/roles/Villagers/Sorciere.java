package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.VillagerRoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.attributes.Power;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class Sorciere extends Role implements Power {

    private boolean nonRespawned = true;

    public Sorciere(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<ItemStack> rolesItems = new ArrayList<>();
        rolesItems.add(new Potion(PotionType.INSTANT_HEAL, 1, true).toItemStack(3));
        rolesItems.add(new Potion(PotionType.REGEN, 1, true).toItemStack(1));
        rolesItems.add(new Potion(PotionType.INSTANT_DAMAGE, 1, true).toItemStack(3));

        return new VillagerRoleIdentity("Sorciere", rolesItems, 20).getIdentity();
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

    @Override
    public void setPower(Boolean p0) {
        nonRespawned =p0;
    }

    @Override
    public Boolean hasPower() {
        return nonRespawned;
    }
}
