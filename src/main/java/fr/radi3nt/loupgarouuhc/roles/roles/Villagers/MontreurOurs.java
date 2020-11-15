package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou.LGFeutre;

import fr.radi3nt.uhc.api.utilis.Maths;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;


public class MontreurOurs extends Role {

    private int radius = 10;

    public MontreurOurs(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("MontreurOurs", WinType.VILLAGE, RoleType.VILLAGER);
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
    public void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {

    }

    @Override
    public void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }

    @Override
    public void discoverRole(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, UHCPlayer lgp) {
        for (UHCPlayer player : game.getGamePlayers()) {
            if (player!=null)
                if (Maths.distanceIn2D(player.getPlayer().getLocation(), lgp.getPlayerStats().getLastLocation()) < radius) {
                    if (player.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                        if (((LGFeutre) player.getGameData().getRole()).affichage.getRoleType() == RoleType.LOUP_GAROU)
                            LoupGarouUHC.broadcastMessage(getPrefix() + ChatColor.GOLD + " Grrrrrr");
                    } else if (player.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU || player.getGameData().getRole().isInfected()) {
                        LoupGarouUHC.broadcastMessage(getPrefix() + ChatColor.GOLD + " Grrrrrr");
                    }
                }
        }
    }

}
