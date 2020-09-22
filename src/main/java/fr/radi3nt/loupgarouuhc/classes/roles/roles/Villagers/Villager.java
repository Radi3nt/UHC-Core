package fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.Location;

public class Villager extends Role {


    public Villager(LGGame game) {
        super(game);
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
    }

    @Override
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {

    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.VILLAGER;
    }


}


