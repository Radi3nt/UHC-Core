// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

public class Location {
    public final int offset;
    public final int line;
    public final int column;

    Location(final int offset, final int line, final int column) {
        this.offset = offset;
        this.column = column;
        this.line = line;
    }

    @Override
    public String toString() {
        return this.line + ":" + this.column;
    }

    @Override
    public int hashCode() {
        return this.offset;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        return this.offset == other.offset && this.column == other.column && this.line == other.line;
    }
}
