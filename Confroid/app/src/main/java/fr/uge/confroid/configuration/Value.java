package fr.uge.confroid.configuration;

import java.util.Map;

public interface Value {
    default boolean isDictionary() {
        return false;
    }

    default boolean isArray() {
        return false;
    }

    default boolean isPrimitive() {
        return false;
    }

    default Map<java.lang.String, Value> getMap() {
        throw new ClassCastException("The value is not a Dictionary");
    }

    default Value[] getArray() {
        throw new ClassCastException("The value is not an Array");
    }

    default Primitive getPrimitive() {
        throw new ClassCastException("The value is not a Primitive");
    }
}
