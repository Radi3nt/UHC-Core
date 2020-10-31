package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
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
    public void night(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void day(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
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


