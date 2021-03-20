package fr.uge.confroid.configuration;

import java.util.Objects;

public class DoubleValue implements Value {
    private Double value;

    public DoubleValue(Double value) {
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
    public Double getDouble() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleValue DoubleValue = (DoubleValue) o;
        return Objects.equals(value, DoubleValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }
}
