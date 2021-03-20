package fr.uge.confroid.configuration;
import java.util.Objects;

public class StringValue implements Value {
    private String value;

    public StringValue(String value) {
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
    public String getString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue stringValue = (StringValue) o;
        return Objects.equals(value, stringValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

