package fr.uge.confroid.configuration;

import java.util.Objects;

public class Boolean implements Value {
    private final java.lang.Boolean value;

    public Boolean(java.lang.Boolean value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.BOOLEAN;
    }

    @Override
    public java.lang.Boolean getBoolean() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boolean aBoolean = (Boolean) o;
        return Objects.equals(value, aBoolean.value);
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
