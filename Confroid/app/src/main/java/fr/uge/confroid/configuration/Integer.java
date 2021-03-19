package fr.uge.confroid.configuration;

import java.util.Objects;

public class Integer implements Value {
    private java.lang.Integer value;

    public Integer(java.lang.Integer value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.INTEGER;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getInteger();
    }

    @Override
    public java.lang.Integer getInteger() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Integer integer = (Integer) o;
        return Objects.equals(value, integer.value);
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
