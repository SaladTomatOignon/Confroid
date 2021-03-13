package fr.uge.confroid.configuration;

import java.util.Map;
import java.util.Objects;

public class Dictionary implements Value {
    private final Map<java.lang.String, Value> values;

    public Dictionary(Map<java.lang.String, Value> values) {
        this.values = Objects.requireNonNull(values);
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.MAP;
    }

    @Override
    public Map<java.lang.String, Value> getMap() {
        return values;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public java.lang.String toString() {
        return Objects.toString(values);
    }
}
