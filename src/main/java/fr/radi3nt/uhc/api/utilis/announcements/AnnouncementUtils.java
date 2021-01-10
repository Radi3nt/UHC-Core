package fr.radi3nt.uhc.api.utilis.announcements;

import fr.radi3nt.uhc.api.lang.Logger;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnnouncementUtils {

    private static final HashSet<Announcement> announcementCache = new HashSet<>();

    public static void main(String[] args) {
        broadcastNewAnnouncements();
    }

    public static void broadcastNewAnnouncements() {
        String[] lines = new String[1];
        /*
        try {
            lines = getLinesFromURL("");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        lines[0] = "(" + LocalDateTime.of(2020, Month.NOVEMBER, 1, 18, 54, 0).toString() + ") " + "Coucou"; //todo
        broadcastLines(lines);
    }

    private static void broadcastLines(String[] lines) {
        for (String line : lines) {
                if (ScheduledAnnouncement.isScheduledAnnouncement(line)) {
                    ScheduledAnnouncement scheduledAnnouncement = ScheduledAnnouncement.theAnnouncement(line);
                    if (!announcementCache.contains(scheduledAnnouncement))
                    if (LocalDateTime.now().isAfter(scheduledAnnouncement.getLocalDateTime()) || LocalDateTime.now().isEqual(scheduledAnnouncement.getLocalDateTime())) {
                        announcementCache.add(scheduledAnnouncement);
                        broadcastMessage(scheduledAnnouncement.getMessage());
                    }
                } else {
                    SimpleAnnouncement announcement = SimpleAnnouncement.theAnnouncement(line);
                        if (!announcementCache.contains(announcement)) {
                            announcementCache.add(announcement);
                            broadcastMessage(line);
                        }
                }
            }
        }

    private static void broadcastMessage(String text) {
        System.out.println(ChatColor.translateAlternateColorCodes('&', text));
        //UHCCore.broadcastMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    private static String[] getLinesFromURL(String url) throws IOException {
        return getLinesFromURL(new URL(url));
    }

    private static String[] getLinesFromURL(URL url) throws IOException {
        List<String> lines = new ArrayList<>();
        InputStream in = url.openStream();
        try {
            lines = IOUtils.readLines(in);
        } catch (IOException e) {
            Logger.getGeneralLogger().log(e);
        } finally {
            IOUtils.closeQuietly(in);
        }

        return lines.toArray(new String[0]);
    }

}
