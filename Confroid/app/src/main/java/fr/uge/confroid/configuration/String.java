package fr.uge.confroid.configuration;

public class String implements Primitive {
    private final java.lang.String string;

    public String(java.lang.String string) {
        this.string = string;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public java.lang.String getString() {
        return string;
    }
}
