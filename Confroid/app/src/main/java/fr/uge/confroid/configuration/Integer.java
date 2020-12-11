package fr.uge.confroid.configuration;

public class Integer implements Primitive {
    private final java.lang.Integer _int;

    public Integer(java.lang.Integer _int) {
        this._int = _int;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public java.lang.Integer getInteger() {
        return _int;
    }
}
