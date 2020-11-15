package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;

import org.bukkit.Location;

public class Corbeau extends Role {

    public Corbeau(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Corbeau", WinType.VILLAGE, RoleType.VILLAGER);
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
