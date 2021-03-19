package fr.uge.confroid.configuration;

import java.util.Objects;

public class Boolean implements Value {
    private java.lang.Boolean value;

    public Boolean(java.lang.Boolean value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.BOOLEAN;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getBoolean();
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
