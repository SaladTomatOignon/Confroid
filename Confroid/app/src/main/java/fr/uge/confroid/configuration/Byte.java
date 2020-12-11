package fr.uge.confroid.configuration;

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
}
