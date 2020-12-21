package fr.radi3nt.uhc.api.player;

import java.lang.reflect.Type;

public class Attribute<T> {

    private T object;

    public Attribute(T object) {
        this.object=object;
    }

    public T getObject() {
        return object;
    }

    public Type getType() {
        return object.getClass();
    }
}
