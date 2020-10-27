package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou.LGFeutre;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;


public class MontreurOurs extends Role {

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
    public void OnNight(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

    }


    private static ArrayList<Location> generateSphere(Location center, int radius) {
        ArrayList<Location> circlesBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();

        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX - x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
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
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
        ArrayList<Location> blocks = generateSphere(lgp.getPlayer().getLocation().getBlock().getLocation(), 10);
        for (LGPlayer player : game.getGamePlayers()) {
            if (blocks.contains(player.getPlayer().getLocation().getBlock().getLocation())) {
                if (player.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU && !player.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                    Bukkit.broadcastMessage(getPrefix() + ChatColor.GOLD + " Grrrrrr");
                }
                if (player.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity()) && ((LGFeutre) player.getGameData().getRole()).affichage.getRoleType() == RoleType.LOUP_GAROU) {
                    Bukkit.broadcastMessage(getPrefix() + ChatColor.GOLD + " Grrrrrr");
                }
            }
        }
    }

}
