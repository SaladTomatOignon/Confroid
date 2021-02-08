package fr.uge.confroid.configuration;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boolean aBoolean = (Boolean) o;
        return Objects.equals(_bool, aBoolean._bool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_bool);
    }
}
