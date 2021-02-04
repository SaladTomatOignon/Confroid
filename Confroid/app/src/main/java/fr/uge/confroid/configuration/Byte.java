package fr.uge.confroid.configuration;
import java.util.Objects;

public class Byte implements Primitive {
    private final byte _byte;

    public Byte(byte _byte) {
        this._byte = _byte;
    }

    @Override
    public boolean isByte() {
        return true;
    }

    @Override
    public java.lang.Byte getByte() {
        return _byte;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Byte aByte = (Byte) o;
        return _byte == aByte._byte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_byte);
    }

}
