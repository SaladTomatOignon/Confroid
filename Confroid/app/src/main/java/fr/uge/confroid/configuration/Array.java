package fr.uge.confroid.configuration;

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
}
