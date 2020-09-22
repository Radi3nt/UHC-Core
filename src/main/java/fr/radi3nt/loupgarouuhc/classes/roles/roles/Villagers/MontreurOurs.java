package fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleType;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGFeutre;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;


public class MontreurOurs extends Role {

    public MontreurOurs(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.MONTREUR_OURS;
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
        ArrayList<Location> blocks = generateSphere(lgp.getPlayer().getLocation().getBlock().getLocation(), 10);
        for (LGPlayer player : game.getGamePlayers()) {
            if (blocks.contains(player.getPlayer().getLocation().getBlock().getLocation())) {
                if (player.getRole().getRoleType() == RoleType.LOUP_GAROU && player.getRole().getRoleSort()!=RoleSort.LG_FEUTRE) {
                    Bukkit.broadcastMessage(prefix + ChatColor.GOLD + " Grrrrrr");
                }
                if (player.getRole().getRoleSort()==RoleSort.LG_FEUTRE && ((LGFeutre) player.getRole()).affichage.getRoleType()==RoleType.LOUP_GAROU) {
                    Bukkit.broadcastMessage(prefix + ChatColor.GOLD + " Grrrrrr");
                }
            }
        }
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

    public static ArrayList<Location> generateSphere(Location center, int radius) {
        ArrayList<Location> circlesBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();

        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX -x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
    }

}
