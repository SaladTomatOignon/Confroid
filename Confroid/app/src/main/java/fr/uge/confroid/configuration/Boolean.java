package fr.uge.confroid.configuration;

public class Boolean implements Primitive {
    private final java.lang.Boolean _bool;

    public Boolean(java.lang.Boolean bool) {
        this._bool = bool;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public java.lang.Boolean getBoolean() {
        return _bool;
    }
}
