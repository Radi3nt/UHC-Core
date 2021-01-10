// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JsonObject extends JsonValue implements Iterable<JsonObject.Member> {
    private final List<String> names;
    private final List<JsonValue> values;
    private transient HashIndexTable table;

    public JsonObject() {
        this.names = new ArrayList<String>();
        this.values = new ArrayList<JsonValue>();
        this.table = new HashIndexTable();
    }

    public JsonObject(final JsonObject object) {
        this(object, false);
    }

    private JsonObject(final JsonObject object, final boolean unmodifiable) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        if (unmodifiable) {
            this.names = Collections.unmodifiableList(object.names);
            this.values = Collections.unmodifiableList(object.values);
        } else {
            this.names = new ArrayList<String>(object.names);
            this.values = new ArrayList<JsonValue>(object.values);
        }
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    @Deprecated
    public static JsonObject readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asObject();
    }

    @Deprecated
    public static JsonObject readFrom(final String string) {
        return JsonValue.readFrom(string).asObject();
    }

    public static JsonObject unmodifiableObject(final JsonObject object) {
        return new JsonObject(object, true);
    }

    public JsonObject add(final String name, final int value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final long value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final float value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final double value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final boolean value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final String value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.table.add(name, this.names.size());
        this.names.add(name);
        this.values.add(value);
        return this;
    }

    public JsonObject set(final String name, final int value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final long value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final float value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final double value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final boolean value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final String value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int index = this.indexOf(name);
        if (index != -1) {
            this.values.set(index, value);
        } else {
            this.table.add(name, this.names.size());
            this.names.add(name);
            this.values.add(value);
        }
        return this;
    }


    public JsonObject remove(final String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        final int index = this.indexOf(name);
        if (index != -1) {
            this.table.remove(index);
            this.names.remove(index);
            this.values.remove(index);
        }
        return this;
    }


    public JsonObject merge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        for (Member member : object) {
            if (this.indexOf(member.name) != -1 && member.getValue().isObject() && get(member.name).isObject()) {
                JsonObject jsonObject = get(member.name).asObject();
                jsonObject.merge(member.getValue().asObject());
            } else {
                this.add(member.getName(), member.getValue());
            }
        }
        return this;
    }


/*
    public JsonObject merge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        ArrayList<Member> arrayList = new ArrayList<>();
        for (Member member : object) {
            arrayList.add(member);
        }
        Collections.reverse(arrayList);
        Member lastJson = new Member("null", new JsonString("null"));
        for (Member member : arrayList) {
            final int index = this.indexOf(member.getName());
            if (index!=0) {
                JsonObject jsonObject= new JsonObject().add(member.getName(), member.getValue());
                jsonObject.add(lastJson.getName(), member.getValue());
                this.set(member.getName(), jsonObject);
            } else {
                this.add(member.getName(), member.getValue());
            }
            lastJson=member;
        }
        return this;
    }

 */

    public JsonValue get(final String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        final int index = this.indexOf(name);
        return (index != -1) ? this.values.get(index) : null;
    }

    public int getInt(final String name, final int defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asInt() : defaultValue;
    }

    public long getLong(final String name, final long defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asLong() : defaultValue;
    }

    public float getFloat(final String name, final float defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asFloat() : defaultValue;
    }

    public double getDouble(final String name, final double defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asDouble() : defaultValue;
    }

    public boolean getBoolean(final String name, final boolean defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asBoolean() : defaultValue;
    }

    public String getString(final String name, final String defaultValue) {
        final JsonValue value = this.get(name);
        return (value != null) ? value.asString() : defaultValue;
    }

    public int size() {
        return this.names.size();
    }

    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }

    public Iterator<Member> iterator() {
        final Iterator<String> namesIterator = this.names.iterator();
        final Iterator<JsonValue> valuesIterator = this.values.iterator();
        return new Iterator<Member>() {
            public boolean hasNext() {
                return namesIterator.hasNext();
            }

            public Member next() {
                final String name = namesIterator.next();
                final JsonValue value = valuesIterator.next();
                return new Member(name, value);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeObjectOpen();
        final Iterator<String> namesIterator = this.names.iterator();
        final Iterator<JsonValue> valuesIterator = this.values.iterator();
        if (namesIterator.hasNext()) {
            writer.writeMemberName(namesIterator.next());
            writer.writeMemberSeparator();
            valuesIterator.next().write(writer);
            while (namesIterator.hasNext()) {
                writer.writeObjectSeparator();
                writer.writeMemberName(namesIterator.next());
                writer.writeMemberSeparator();
                valuesIterator.next().write(writer);
            }
        }
        writer.writeObjectClose();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.names.hashCode();
        result = 31 * result + this.values.hashCode();
        return result;
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
        final JsonObject other = (JsonObject) obj;
        return this.names.equals(other.names) && this.values.equals(other.values);
    }

    int indexOf(final String name) {
        final int index = this.table.get(name);
        if (index != -1 && name.equals(this.names.get(index))) {
            return index;
        }
        return this.names.lastIndexOf(name);
    }

    private synchronized void readObject(final ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    private void updateHashIndex() {
        for (int size = this.names.size(), i = 0; i < size; ++i) {
            this.table.add(this.names.get(i), i);
        }
    }

    public static class Member {
        private final String name;
        private final JsonValue value;

        Member(final String name, final JsonValue value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public JsonValue getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + this.name.hashCode();
            result = 31 * result + this.value.hashCode();
            return result;
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
            final Member other = (Member) object;
            return this.name.equals(other.name) && this.value.equals(other.value);
        }
    }

    static class HashIndexTable {
        private final byte[] hashTable;

        public HashIndexTable() {
            this.hashTable = new byte[32];
        }

        public HashIndexTable(final HashIndexTable original) {
            this.hashTable = new byte[32];
            System.arraycopy(original.hashTable, 0, this.hashTable, 0, this.hashTable.length);
        }

        void add(final String name, final int index) {
            final int slot = this.hashSlotFor(name);
            if (index < 255) {
                this.hashTable[slot] = (byte) (index + 1);
            } else {
                this.hashTable[slot] = 0;
            }
        }

        void remove(final int index) {
            for (int i = 0; i < this.hashTable.length; ++i) {
                if (this.hashTable[i] == index + 1) {
                    this.hashTable[i] = 0;
                } else if (this.hashTable[i] > index + 1) {
                    final byte[] hashTable = this.hashTable;
                    final int n = i;
                    --hashTable[n];
                }
            }
        }

        int get(final Object name) {
            final int slot = this.hashSlotFor(name);
            return (this.hashTable[slot] & 0xFF) - 1;
        }

        private int hashSlotFor(final Object element) {
            return element.hashCode() & this.hashTable.length - 1;
        }
    }
}
