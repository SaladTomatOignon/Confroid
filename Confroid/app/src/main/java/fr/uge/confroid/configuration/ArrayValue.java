package fr.uge.confroid.configuration;

import java.util.Arrays;

public class ArrayValue implements Value {
    private Value[] values;

    public ArrayValue(Value[] values) {
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
    public String preview() {
        int size = Configuration.filterKeywords(this).getArray().length;
        return size + " items";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayValue array1 = (ArrayValue) o;
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
