package fr.uge.confroid.configuration;

import java.util.Objects;

public class Long implements Value {
    private java.lang.Long value;

    public Long(java.lang.Long value) {
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
    public java.lang.Long getLong() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Long aLong = (Long) o;
        return Objects.equals(value, aLong.value);
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
