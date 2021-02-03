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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
