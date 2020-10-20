package fr.radi3nt.loupgarouuhc.classes.stats;

import fr.radi3nt.loupgarouuhc.utilis.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.plugin;

public class HoloStats {

    private static final ArrayList<HoloStats> cachedHolo = new ArrayList<>();

    private final String text = ChatColor.translateAlternateColorCodes('&', ChatColor.AQUA + "#%place%" + ChatColor.GRAY + " » " + ChatColor.YELLOW + "%name% " + ChatColor.GRAY + "-" + ChatColor.GREEN + "%points%");

    private Location location;
    private final ArrayList<Hologram> holograms = new ArrayList<>();
    private final Integer maxPlayers;

    public HoloStats(Location location, Integer maxPlayers) {
        this.location = location;
        this.maxPlayers = maxPlayers;
        cachedHolo.add(this);
        double shift = 0;
        ArrayList<String> notRanked = new ArrayList<>();
        for (int i = 0; i < maxPlayers; i++) {
            Location armorLoc = new Location(location.getWorld(), location.getX(), location.getY() - shift, location.getZ());
            String name = " ";
            int value = 0;
            Config config = Config.createConfig(plugin.getDataFolder() + "", "players.yml");
            try {
                for (String playerNames : config.getConfiguration().getStringList("Players")) {
                    Config config1 = Config.createConfig(plugin.getDataFolder() + "/players", playerNames + ".yml");
                    if (config1.getConfiguration().getInt("Stats" + ".points") >= value && !notRanked.contains(playerNames)) {
                        value = config1.getConfiguration().getInt("Stats" + ".points");
                        name = playerNames;
                    }
                }
            } catch (NullPointerException e) {

            }
            if (!name.equals(" ")) {
                notRanked.add(name);
                Config config1 = Config.createConfig(plugin.getDataFolder() + "/players", name + ".yml");
                int games = config1.getConfiguration().getInt("Stats" + ".games");
                int wins = config1.getConfiguration().getInt("Stats" + ".wins");
                int kills = config1.getConfiguration().getInt("Stats" + ".kills");
                int points = config1.getConfiguration().getInt("Stats" + ".points");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(' ');
                DecimalFormat df = new DecimalFormat("###,###", symbols);
                //DecimalFormat df = new DecimalFormat("###,###.00", symbols);
                String pointsS = df.format(points);

                //Hologram hologram = new Hologram(armorLoc, ChatColor.GOLD + "" + (notRanked.size()) + ". " + ChatColor.BOLD + name + ChatColor.GOLD + " |" + ChatColor.GRAY + " points: " + ChatColor.AQUA + points +ChatColor.GRAY + ", game: " + ChatColor.AQUA + games + ChatColor.GRAY + ", wins: " + ChatColor.AQUA + wins + ChatColor.GRAY + ", kills: " + ChatColor.AQUA + kills);
                Hologram hologram = new Hologram(armorLoc, text.replace("%place%", String.valueOf(notRanked.size())).replace("%name%", name).replace("%points%", ChatColor.RESET + " points: " + ChatColor.GREEN + pointsS + ChatColor.RESET + ", game: " + ChatColor.GREEN + games + ChatColor.RESET + ", wins: " + ChatColor.GREEN + wins + ChatColor.RESET + ", kills: " + ChatColor.GREEN + kills));
                hologram.displayForAll();
                holograms.add(hologram);
                shift = shift + 0.3;
            }
        }
    }

    public static void createHoloStatsScoreboards() {
        Config config = Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
        ArrayList<String> loc = (ArrayList<String>) config.getConfiguration().getStringList("Holograms");
        for (String locations : loc) {
            String [] coords = locations.split(" ");
            Location holoLoc = new Location(Bukkit.getWorld(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]));
            new HoloStats(holoLoc, 9);
        }
    }

    public static void createHoloStats(Location loc, Integer maxPlayers) {
        new HoloStats(loc, maxPlayers);
        String location = loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ();
        ArrayList<String> locList;
        Config config = Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
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
        Config config = Config.createConfig(plugin.getDataFolder() + "", "stats.yml");
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
        for (int i = 0; i < holograms.size(); i++) {
            Hologram hologram = holograms.get(i);
            hologram.hideFromAll();
            holograms.remove(hologram);
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

    public ArrayList<Hologram> getHologramsStand() {
        return holograms;
    }

    public static HoloStats theHoloStats(Location location) {
        for (HoloStats holo : cachedHolo) {
            if (holo.getLocation() == location) {
                return holo;
            }
        }
        return null;
    }
}
