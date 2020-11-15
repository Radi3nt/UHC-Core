// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

public class ParseException extends RuntimeException {
    private final Location location;

    ParseException(final String message, final Location location) {
        super(message + " at " + location);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    @Deprecated
    public int getOffset() {
        return this.location.offset;
    }

    @Deprecated
    public int getLine() {
        return this.location.line;
    }

    @Deprecated
    public int getColumn() {
        return this.location.column;
    }
}
