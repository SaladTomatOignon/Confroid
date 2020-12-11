package fr.uge.confroid.configuration;

import java.util.Map;
import java.util.Objects;

public class Dictionary implements Value {
    private final Map<java.lang.String, Value> map;

    public Dictionary(Map<java.lang.String, Value> map) {
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public Map<java.lang.String, Value> getMap() {
        return map;
    }

    @Override
    public boolean isDictionary() {
        return true;
    }
}
