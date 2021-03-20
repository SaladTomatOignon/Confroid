package fr.uge.confroid.configuration;

import java.util.Arrays;

public class Array implements Value {
    private Value[] values;

    public Array(Value[] values) {
        this.values = values;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.ARRAY;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.values = value.getArray();
    }

    @Override
    public Value[] getArray() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array array1 = (Array) o;
        return Arrays.equals(values, array1.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
