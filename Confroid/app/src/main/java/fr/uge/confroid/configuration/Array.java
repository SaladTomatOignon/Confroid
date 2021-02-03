package fr.uge.confroid.configuration;

import java.util.Arrays;

public class Array implements Value {
    private final Value[] array;

    public Array(Value[] array) {
        this.array = array;
    }

    @Override
    public Value[] getArray() {
        return array;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array array1 = (Array) o;
        return Arrays.equals(array, array1.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
