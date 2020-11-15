// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

public abstract class JsonHandler<A, O> {
    JsonParser parser;

    protected Location getLocation() {
        return this.parser.getLocation();
    }

    public void startNull() {
    }

    public void endNull() {
    }

    public void startBoolean() {
    }

    public void endBoolean(final boolean value) {
    }

    public void startString() {
    }

    public void endString(final String string) {
    }

    public void startNumber() {
    }

    public void endNumber(final String string) {
    }

    public A startArray() {
        return null;
    }

    public void endArray(final A array) {
    }

    public void startArrayValue(final A array) {
    }

    public void endArrayValue(final A array) {
    }

    public O startObject() {
        return null;
    }

    public void endObject(final O object) {
    }

    public void startObjectName(final O object) {
    }

    public void endObjectName(final O object, final String name) {
    }

    public void startObjectValue(final O object, final String name) {
    }

    public void endObjectValue(final O object, final String name) {
    }
}
