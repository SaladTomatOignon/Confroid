package fr.uge.confroid.configuration;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float aFloat = (Float) o;
        return Objects.equals(_float, aFloat._float);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_float);
    }
}
