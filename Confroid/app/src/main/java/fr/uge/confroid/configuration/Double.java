package fr.uge.confroid.configuration;

import java.util.Objects;

public class Double implements Value {
    private java.lang.Double value;

    public Double(java.lang.Double value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.DOUBLE;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getDouble();
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
