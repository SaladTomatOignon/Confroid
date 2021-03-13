package fr.uge.confroid.configuration;

import java.util.Objects;

public class Float implements Value {
    private final java.lang.Float value;

    public Float(java.lang.Float value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.FLOAT;
    }

    @Override
    public java.lang.Float getFloat() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float aFloat = (Float) o;
        return Objects.equals(value, aFloat.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public java.lang.String toString() {
        return Objects.toString(value);
    }
}
