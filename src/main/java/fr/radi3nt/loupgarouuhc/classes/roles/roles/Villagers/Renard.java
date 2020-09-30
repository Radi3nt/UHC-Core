package fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefixPrivé;

public class Renard extends Role {

    private final int time = 0;
    private boolean canSee;
    private int radius = 20;

    public Renard(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.RENARD;
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
        if (time == 3) {
            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.GOLD + " Tu peut voir si un joueur est loup garou ou pas en faisant /lg role see <player>, tu pourras flairer un joueur encore " + ChatColor.YELLOW + (3 - time) + ChatColor.GOLD + " fois");
            this.canSee = true;
        }
    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        canSee = false;
    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {

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

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public boolean canSee() {
        return canSee;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
