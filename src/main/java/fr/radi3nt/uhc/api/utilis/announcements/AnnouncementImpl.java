package fr.radi3nt.uhc.api.utilis.announcements;

import java.util.Objects;

public class AnnouncementImpl implements Announcement {

    private final String message;

    protected AnnouncementImpl(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnouncementImpl that = (AnnouncementImpl) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
