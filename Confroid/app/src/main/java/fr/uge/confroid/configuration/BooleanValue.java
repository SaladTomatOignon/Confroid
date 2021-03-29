package fr.uge.confroid.configuration;

import java.util.Objects;

public class BooleanValue implements Value {
    private Boolean value;

    public BooleanValue(Boolean value) {
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
    public Boolean getBoolean() {
        return value;
    }

    @Override
    public BooleanValue deepCopy() {
        return new BooleanValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanValue booleanValue = (BooleanValue) o;
        return Objects.equals(value, booleanValue.value);
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
