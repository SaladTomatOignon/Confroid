package fr.uge.confroid.configuration;

import java.util.Objects;

public class FloatValue implements Value {
    private Float value;

    public FloatValue(Float value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.FLOAT;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getFloat();
    }

    @Override
    public Float getFloat() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatValue floatValue = (FloatValue) o;
        return Objects.equals(value, floatValue.value);
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
