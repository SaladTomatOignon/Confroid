package fr.uge.confroid.configuration;

import java.util.Objects;

public class IntegerValue implements Value {
    private Integer value;

    public IntegerValue(Integer value) {
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
    public Integer getInteger() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerValue integerValue = (IntegerValue) o;
        return Objects.equals(value, integerValue.value);
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
