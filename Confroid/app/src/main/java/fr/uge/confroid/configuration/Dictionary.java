package fr.uge.confroid.configuration;

import java.util.Map;
import java.util.Objects;

public class Dictionary implements Value {
    private Map<String, Value> values;

    public Dictionary(Map<String, Value> values) {
        this.values = Objects.requireNonNull(values);
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.MAP;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.values = value.getMap();
    }

    @Override
    public Map<String, Value> getMap() {
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
    public String toString() {
        return Objects.toString(values);
    }
}
