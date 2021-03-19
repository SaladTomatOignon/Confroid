package fr.uge.confroid.configuration;
import java.util.Objects;

public class Byte implements Value {
    private byte value;

    public Byte(byte value) {
        this.value = value;
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.BYTE;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.value = value.getByte();
    }

    @Override
    public java.lang.Byte getByte() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Byte aByte = (Byte) o;
        return value == aByte.value;
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
