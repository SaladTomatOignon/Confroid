package fr.uge.confroid.configuration;
import java.util.Objects;

public class ByteValue implements Value {
    private byte value;

    public ByteValue(byte value) {
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
    public Byte getByte() {
        return value;
    }

    @Override
    public ByteValue deepCopy() {
        return new ByteValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteValue byteValue = (ByteValue) o;
        return value == byteValue.value;
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
