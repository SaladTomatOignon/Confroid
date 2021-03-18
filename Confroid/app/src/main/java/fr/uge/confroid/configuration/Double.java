package fr.uge.confroid.configuration;

import java.util.Objects;

public class Double implements Value {
    private final java.lang.Double value;

    public Double(java.lang.Double value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.DOUBLE;
    }

    @Override
    public java.lang.Double getDouble() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Double aDouble = (Double) o;
        return Objects.equals(value, aDouble.value);
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
