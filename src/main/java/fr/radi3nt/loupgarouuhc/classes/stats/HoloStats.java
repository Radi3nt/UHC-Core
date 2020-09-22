package fr.radi3nt.loupgarouuhc.classes.stats;

import fr.radi3nt.loupgarouuhc.classes.config.Config;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class HoloStats {

    private static ArrayList<HoloStats> cachedHolo = new ArrayList<>();

    private Location location;
    private ArrayList<ArmorStand> standArrayList = new ArrayList<>();
    private Integer maxPlayers;

    public HoloStats(Location location, Integer maxPlayers) {
        this.location = location;
        this.maxPlayers=maxPlayers;
        cachedHolo.add(this);
        double shift = 0;
        ArrayList<String> notRanked = new ArrayList<>();
        for (int i = 0; i < maxPlayers; i++) {
            Location armorLoc = new Location(location.getWorld(), location.getX(), location.getY() - shift, location.getZ());
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(armorLoc, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setSmall(true);
            armorStand.setBasePlate(false);
            armorStand.setInvulnerable(true);
            String name = " ";
            int value = 0;
            fr.radi3nt.loupgarouuhc.classes.config.Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "", "players.yml");
            try {
                for (String playerNames : config.getConfiguration().getStringList("Players")) {
                    fr.radi3nt.loupgarouuhc.classes.config.Config config1 = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "/players", playerNames + ".yml");
                    if (config1.getConfiguration().getInt(playerNames + ".points") >= value && !notRanked.contains(playerNames)) {
                        value = config1.getConfiguration().getInt(playerNames + ".points");
                        name = playerNames;
                    }
                }
            } catch (NullPointerException e) {

            }
            if (name.equals(" ")) {
                armorStand.remove();
            } else {
                notRanked.add(name);
                fr.radi3nt.loupgarouuhc.classes.config.Config config1 = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "/players", name + ".yml");
                standArrayList.add(armorStand);
                int games = config1.getConfiguration().getInt( name + ".games");
                int wins = config1.getConfiguration().getInt( name + ".wins");
                int kills = config1.getConfiguration().getInt( name + ".kills");
                int points = config1.getConfiguration().getInt( name + ".points");
                armorStand.setCustomName(ChatColor.GOLD + "" + (notRanked.size()) + ". " + ChatColor.BOLD + name + ChatColor.GOLD + " |" + ChatColor.GRAY + " points: " + ChatColor.AQUA + points +ChatColor.GRAY + ", game: " + ChatColor.AQUA + games + ChatColor.GRAY + ", wins: " + ChatColor.AQUA + wins + ChatColor.GRAY + ", kills: " + ChatColor.AQUA + kills);
                armorStand.setCustomNameVisible(true);
                shift = shift + 0.3;
            }
        }
    }

    public static void createHoloStatsScoreboards() {
        fr.radi3nt.loupgarouuhc.classes.config.Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
        ArrayList<String> loc = (ArrayList<String>) config.getConfiguration().getStringList("Holograms");
        for (String locations : loc) {
            String [] coords = locations.split(" ");
            Location holoLoc = new Location(Bukkit.getWorld(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]));
            new HoloStats(holoLoc, 5);
        }
    }

    public static void createHoloStats(Location loc, Integer maxPlayers) {
        new HoloStats(loc, maxPlayers);
        String location = loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ();
        ArrayList<String> locList;
        fr.radi3nt.loupgarouuhc.classes.config.Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
        try {
             locList= (ArrayList<String>) config.getConfiguration().getStringList("Holograms");
        } catch (Exception e) {
            locList=new ArrayList<>();
        }
        locList.add(location);
        config.getConfiguration().set("Holograms", locList);
        config.saveConfig();
    }

    public static ArrayList<HoloStats> getCachedHolo() {
        return cachedHolo;
    }

    public static ArrayList<HoloStats> getHolos() {
        fr.radi3nt.loupgarouuhc.classes.config.Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
        ArrayList<HoloStats> arrayList = new ArrayList<>();
        ArrayList<String> loc = (ArrayList<String>) config.getConfiguration().getStringList("Holograms");
        for (String locations : loc) {
            String [] coords = locations.split(" ");
            Location holoLoc = new Location(Bukkit.getWorld(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]));
            arrayList.add(HoloStats.theHoloStats(holoLoc));
        }
        return arrayList;
    }

    public void remove() {
        /*
        Iterator<ArmorStand> it = standArrayList.iterator();
        while (it.hasNext()) {
            ArmorStand armorStand = it.next();
            standArrayList.remove(armorStand);
            armorStand.remove();
            it.remove();
        }

         */
        for (int i = 0; i < standArrayList.size(); i++) {
            ArmorStand armorStand = standArrayList.get(i);
            armorStand.remove();
            standArrayList.remove(armorStand);
            i--;
        }
        cachedHolo.remove(this);
    }

    public static void updateAll() {
        /*
        Iterator<HoloStats> it = HoloStats.getCachedHolo().iterator();
        while (it.hasNext()) {
            HoloStats holo = it.next();
            holo.remove();
            it.remove();
        }
         */
        for (int i = 0; i < cachedHolo.size(); i++) {
            HoloStats holo = cachedHolo.get(i);
            holo.remove();
            i--;
        }
        HoloStats.createHoloStatsScoreboards();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.remove();
        new HoloStats(location, this.maxPlayers);
    }

    public ArrayList<ArmorStand> getStandArrayList() {
        return standArrayList;
    }

    public static HoloStats theHoloStats(Location location) {
        for (HoloStats holo : cachedHolo) {
            if (holo.getLocation()==location) {
                return holo;
            }
        }
        return null;
    }
}
