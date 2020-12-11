package fr.uge.confroid.configuration;

public class Float implements Primitive {
    private final java.lang.Float _float;

    public Float(java.lang.Float _float) {
        this._float = _float;
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public java.lang.Float getFloat() {
        return _float;
    }
}
