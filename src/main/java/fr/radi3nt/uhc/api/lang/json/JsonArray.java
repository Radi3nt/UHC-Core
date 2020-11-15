// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends JsonValue implements Iterable<JsonValue> {
    private final List<JsonValue> values;

    public JsonArray() {
        this.values = new ArrayList<JsonValue>();
    }

    public JsonArray(final JsonArray array) {
        this(array, false);
    }

    private JsonArray(final JsonArray array, final boolean unmodifiable) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        if (unmodifiable) {
            this.values = Collections.unmodifiableList(array.values);
        } else {
            this.values = new ArrayList<JsonValue>(array.values);
        }
    }

    @Deprecated
    public static JsonArray readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asArray();
    }

    @Deprecated
    public static JsonArray readFrom(final String string) {
        return JsonValue.readFrom(string).asArray();
    }

    public static JsonArray unmodifiableArray(final JsonArray array) {
        return new JsonArray(array, true);
    }

    public JsonArray add(final int value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final long value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final float value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final double value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final boolean value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final String value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(value);
        return this;
    }

    public JsonArray set(final int index, final int value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final long value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final float value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final double value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final boolean value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final String value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.set(index, value);
        return this;
    }

    public JsonArray remove(final int index) {
        this.values.remove(index);
        return this;
    }

    public int size() {
        return this.values.size();
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public JsonValue get(final int index) {
        return this.values.get(index);
    }

    public List<JsonValue> values() {
        return Collections.unmodifiableList(this.values);
    }

    public Iterator<JsonValue> iterator() {
        final Iterator<JsonValue> iterator = this.values.iterator();
        return new Iterator<JsonValue>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }

            public JsonValue next() {
                return iterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeArrayOpen();
        final Iterator<JsonValue> iterator = this.iterator();
        if (iterator.hasNext()) {
            iterator.next().write(writer);
            while (iterator.hasNext()) {
                writer.writeArraySeparator();
                iterator.next().write(writer);
            }
        }
        writer.writeArrayClose();
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
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
        final JsonArray other = (JsonArray) object;
        return this.values.equals(other.values);
    }
}
