package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;

import org.bukkit.Location;

public class Villager extends Role {


    public Villager(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Villager", WinType.VILLAGE, RoleType.VILLAGER);
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
        lgp.getPlayer().setMaxHealth(20F);
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


