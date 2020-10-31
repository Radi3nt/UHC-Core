package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.VillagerRoleIdentity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Mineur extends Role {

    public Mineur(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<ItemStack> rolesItems = new ArrayList<>();
        rolesItems.add(new ItemStack(Material.DIAMOND_PICKAXE, 1));
        return new VillagerRoleIdentity("Mineur", rolesItems, 20).getIdentity();
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

    }

    @Override
    public void discoverRole(LGGame game, LGPlayer lgp) {

    }
}