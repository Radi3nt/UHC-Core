package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.VillagerRoleIdentity;
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
    protected void night(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void day(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void newEpisode(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {

    }

    @Override
    protected void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }

    @Override
    protected void discoverRole(LGGame game, UHCPlayer lgp) {

    }
}