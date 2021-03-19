package fr.uge.confroid.configuration;
import java.util.Objects;

public class String implements Value {
    private java.lang.String value;

    public String(java.lang.String value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.STRING;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getString();
    }

    @Override
    public java.lang.String getString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        String string1 = (String) o;
        return Objects.equals(value, string1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public java.lang.String toString() {
        return value;
    }
}

