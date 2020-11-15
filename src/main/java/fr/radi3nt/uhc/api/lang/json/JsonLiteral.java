// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

import java.io.IOException;

class JsonLiteral extends JsonValue {
    private final String value;
    private final boolean isNull;
    private final boolean isTrue;
    private final boolean isFalse;

    JsonLiteral(final String value) {
        this.value = value;
        this.isNull = "null".equals(value);
        this.isTrue = "true".equals(value);
        this.isFalse = "false".equals(value);
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeLiteral(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public boolean isTrue() {
        return this.isTrue;
    }

    @Override
    public boolean isFalse() {
        return this.isFalse;
    }

    @Override
    public boolean isBoolean() {
        return this.isTrue || this.isFalse;
    }

    @Override
    public boolean asBoolean() {
        return this.isNull ? super.asBoolean() : this.isTrue;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        final JsonLiteral other = (JsonLiteral) object;
        return this.value.equals(other.value);
    }
}
