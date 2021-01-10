package fr.radi3nt.uhc.api.utilis.announcements;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduledAnnouncement extends AnnouncementImpl {

    private final LocalDateTime localDateTime;

    public ScheduledAnnouncement(String message, LocalDateTime localDateTime) {
        super(message);
        this.localDateTime = localDateTime;
    }

    public static ScheduledAnnouncement theAnnouncement(String line) {
        if (isScheduledAnnouncement(line)) {
            String dateAndMessage = line.replace("(", "");
            String dateOnly = dateAndMessage.split("\\) ")[0];
            String message = line.replace("(" + dateOnly + ") ", "");
            LocalDateTime dateTime = LocalDateTime.parse(dateOnly);
            return new ScheduledAnnouncement(message, dateTime);
        }
        return null;
    }

    public static boolean isScheduledAnnouncement(String line) {
        if (line.startsWith("("))
            if (line.contains(") "))
                return line.split("\\) ").length > 1;
            return false;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Announcement) {
            if (!o.equals(this))
                return false;
            if (o instanceof ScheduledAnnouncement) {
                return getLocalDateTime().equals(((ScheduledAnnouncement) o).getLocalDateTime());
            }
            return true;
        }
        return false;
    }
}
