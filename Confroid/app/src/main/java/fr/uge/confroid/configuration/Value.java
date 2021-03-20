package fr.uge.confroid.configuration;

import java.util.Map;

public interface Value {

    ValueTypes valueType();


    default boolean isMap() {
        return valueType().equals(ValueTypes.MAP);
    }

    default boolean isByte() {
        return valueType().equals(ValueTypes.BYTE);
    }

    default boolean isArray() {
        return valueType().equals(ValueTypes.ARRAY);
    }

    default boolean isFloat() {
        return valueType().equals(ValueTypes.FLOAT);
    }

    default boolean isDouble() {
        return valueType().equals(ValueTypes.DOUBLE);
    }

    default boolean isString() {
        return valueType().equals(ValueTypes.STRING);
    }

    default boolean isInteger() {
        return valueType().equals(ValueTypes.INTEGER);
    }

    default boolean isLong() {
        return valueType().equals(ValueTypes.LONG);
    }

    default boolean isBoolean() {
        return valueType().equals(ValueTypes.BOOLEAN);
    }

    default boolean isPrimitive() {
        return valueType().isPrimitive();
    }


    void setValue(Value value);


    default Map<String, Value> getMap() {
        throw new ClassCastException("The value is not a Dictionary");
    }

    default Value[] getArray() {
        throw new ClassCastException("The value is not an Array");
    }

    default String getString() {
        throw new ClassCastException("The literal is not a String");
    }

    default Byte getByte() {
        throw new ClassCastException("The literal is not a Byte");
    }

    default Float getFloat() {
        throw new ClassCastException("The literal is not a Float");
    }

    default Double getDouble() {
        throw new ClassCastException("The literal is not a Double");
    }

    default Integer getInteger() {
        throw new ClassCastException("The literal is not an Integer");
    }

    default Long getLong() {
        throw new ClassCastException("The literal is not a Long");
    }

    default Boolean getBoolean() {
        throw new ClassCastException("The literal is not a Boolean");
    }
}
