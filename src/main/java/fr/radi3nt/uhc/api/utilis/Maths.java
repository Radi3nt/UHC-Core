package fr.radi3nt.uhc.api.utilis;

import org.bukkit.Location;

import java.util.ArrayList;

public class Maths {

    /**
     * This method use the pythagoras theorem
     *
     * @param point1 first point
     * @param point2 2nd point
     * @return distance between two point
     */
    public static double distanceIn2D(Location point1, Location point2) {
        double distance;

        double x1 = point1.getX();
        double z1 = point1.getZ();

        double x2 = point2.getX();
        double z2 = point2.getZ();

        x1 = x1 - x2;
        z1 = z1 - z2;

        if (x1 < 0) {
            x1 = -x1;
        }
        if (z1 < 0) {
            z1 = -z1;
        }

        double x = (int) x1 * x1;
        double z = (int) z1 * z1;

        distance = x + z;
        distance = Math.sqrt(distance);

        return distance;
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


}
