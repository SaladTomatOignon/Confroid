package fr.uge.confroid.front.models;

import java.util.Objects;

import fr.uge.confroid.configuration.Value;

public class EditorSession {
    private final String name;
    private final Value value;

    public EditorSession(String name, Value value) {
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value newValue) {
        value.setValue(newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorSession that = (EditorSession) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
