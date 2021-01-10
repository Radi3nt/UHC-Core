package fr.radi3nt.uhc.api.utilis.announcements;

import java.time.LocalDateTime;

public class SimpleAnnouncement extends AnnouncementImpl {

    public SimpleAnnouncement(String message) {
        super(message);
    }

    public static SimpleAnnouncement theAnnouncement(String line) {
        return new SimpleAnnouncement(line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Announcement) {
            return getMessage().equals(((Announcement) o).getMessage());
        }
        return false;
    }
}
