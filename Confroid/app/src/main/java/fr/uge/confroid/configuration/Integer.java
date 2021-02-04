package fr.uge.confroid.configuration;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Integer integer = (Integer) o;
        return Objects.equals(_int, integer._int);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_int);
    }
}
