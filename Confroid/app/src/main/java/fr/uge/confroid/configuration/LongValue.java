package fr.uge.confroid.configuration;

import java.util.Objects;

public class LongValue implements Value {
    private Long value;

    public LongValue(Long value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.LONG;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getLong();
    }

    @Override
    public Long getLong() {
        return value;
    }

    @Override
    public LongValue deepCopy() {
        return new LongValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongValue longValue = (LongValue) o;
        return Objects.equals(value, longValue.value);
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
