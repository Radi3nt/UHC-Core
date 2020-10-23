package fr.radi3nt.loupgarouuhc.utilis;

import org.bukkit.Location;

public class Maths {

    /**
     * This method use the pythagoras theorem
     *
     * @param point1 first point
     * @param point2 2nd point
     * @return distance between two point
     */
    public static double getDistanceBetween2Points(Location point1, Location point2) {
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

}
