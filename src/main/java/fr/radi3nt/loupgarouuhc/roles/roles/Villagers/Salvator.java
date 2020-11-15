package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.loupgarouuhc.roles.roles.attributes.Power;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefixPrivé;

public class Salvator extends Role implements Power {

    private boolean canSee = false;
    private UHCPlayer lastPlayer = null;

    public Salvator(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Salvator", WinType.VILLAGE, RoleType.VILLAGER);
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
        this.canSee = true;
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i==1) {
                    lgp.sendMessage(getPrefix() + " " + getPrefixPrivé() + ChatColor.GOLD + " Tu peut proteger un joueur avec la commande /lg role save <player>");
                }
                if (!canSee) {
                    cancel();
                }
                if (i == 30 * 20) {
                    cancel();
                    canSee = false;
                }
                i++;
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1, 1L);
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

    @Override
    public void setPower(Boolean p0) {
        this.canSee=p0;
    }

    @Override
    public Boolean hasPower() {
        return canSee;
    }

    public UHCPlayer getLastPlayer() {
        return lastPlayer;
    }
}
