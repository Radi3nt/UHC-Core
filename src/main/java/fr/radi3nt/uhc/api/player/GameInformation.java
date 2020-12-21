package fr.radi3nt.uhc.api.player;

import fr.radi3nt.uhc.api.game.UHCGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameInformation {

    private HashMap<String, Attribute<?>> attributes = new HashMap<>();

    public void putAttribute(String key, Attribute<?> attribute) {
        attributes.put(key, attribute);
    }

    public boolean puIfAbsentAttribute(String key, Attribute<?> attribute) {
        if (!attributes.containsKey(key))
            putAttribute(key, attribute);
        return !attributes.containsKey(key);
    }

    public Attribute<?> getAttribute(String key) {
         return attributes.get(key);
    }
    public Attribute<?> getAttributeOrDefault(String key, Attribute<?> object) {
         return attributes.getOrDefault(key, object);
    }

    public Attribute<?> removeAttribute(String key) {
        return attributes.remove(key);
    }

}
