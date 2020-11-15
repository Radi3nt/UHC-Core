// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

import java.io.*;

public abstract class JsonValue implements Serializable {
    @Deprecated
    public static final JsonValue TRUE;
    @Deprecated
    public static final JsonValue FALSE;
    @Deprecated
    public static final JsonValue NULL;

    static {
        TRUE = new JsonLiteral("true");
        FALSE = new JsonLiteral("false");
        NULL = new JsonLiteral("null");
    }

    JsonValue() {
    }

    @Deprecated
    public static JsonValue readFrom(final Reader reader) throws IOException {
        return Json.parse(reader);
    }

    @Deprecated
    public static JsonValue readFrom(final String text) {
        return Json.parse(text);
    }

    @Deprecated
    public static JsonValue valueOf(final int value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final long value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final float value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final double value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final String string) {
        return Json.value(string);
    }

    @Deprecated
    public static JsonValue valueOf(final boolean value) {
        return Json.value(value);
    }

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + this.toString());
    }

    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + this.toString());
    }

    public int asInt() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public long asLong() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public float asFloat() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public double asDouble() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public String asString() {
        throw new UnsupportedOperationException("Not a string: " + this.toString());
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException("Not a boolean: " + this.toString());
    }

    public void writeTo(final Writer writer) throws IOException {
        this.writeTo(writer, WriterConfig.MINIMAL);
    }

    public void writeTo(final Writer writer, final WriterConfig config) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        final WritingBuffer buffer = new WritingBuffer(writer, 128);
        this.write(config.createWriter(buffer));
        buffer.flush();
    }

    @Override
    public String toString() {
        return this.toString(WriterConfig.MINIMAL);
    }

    public String toString(final WriterConfig config) {
        final StringWriter writer = new StringWriter();
        try {
            this.writeTo(writer, config);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return writer.toString();
    }

    @Override
    public boolean equals(final Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    abstract void write(final JsonWriter p0) throws IOException;
}
