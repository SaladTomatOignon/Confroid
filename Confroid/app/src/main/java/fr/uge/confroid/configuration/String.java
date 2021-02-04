package fr.uge.confroid.configuration;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        String string1 = (String) o;
        return Objects.equals(string, string1.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}

