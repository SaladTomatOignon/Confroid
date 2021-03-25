package fr.uge.confroid.configuration;

import java.util.Map;

/**
 * Representation of a value stored by Confroid.
 */
public interface Value {

    /**
     * Gets the type of the value.
     */
    ValueTypes valueType();

    /**
     * Gets a value indicating whether the value is a map.
     */
    default boolean isMap() {
        return valueType().equals(ValueTypes.MAP);
    }

    /**
     * Gets a value indicating whether the value is a byte.
     */
    default boolean isByte() {
        return valueType().equals(ValueTypes.BYTE);
    }

    /**
     * Gets a value indicating whether the value is an array.
     */
    default boolean isArray() {
        return valueType().equals(ValueTypes.ARRAY);
    }

    /**
     * Gets a value indicating whether the value is a float.
     */
    default boolean isFloat() {
        return valueType().equals(ValueTypes.FLOAT);
    }

    /**
     * Gets a value indicating whether the value is a double.
     */
    default boolean isDouble() {
        return valueType().equals(ValueTypes.DOUBLE);
    }

    /**
     * Gets a value indicating whether the value is a string.
     */
    default boolean isString() {
        return valueType().equals(ValueTypes.STRING);
    }

    /**
     * Gets a value indicating whether the value is an integer.
     */
    default boolean isInteger() {
        return valueType().equals(ValueTypes.INTEGER);
    }

    /**
     * Gets a value indicating whether the value is a long.
     */
    default boolean isLong() {
        return valueType().equals(ValueTypes.LONG);
    }

    /**
     * Gets a value indicating whether the value is a boolean.
     */
    default boolean isBoolean() {
        return valueType().equals(ValueTypes.BOOLEAN);
    }

    /**
     * Gets a value indicating whether the value is a primitive.
     */
    default boolean isPrimitive() {
        return valueType().isPrimitive();
    }

    /**
     * Updates the value.
     * @param value new value.
     */
    void setValue(Value value);

    /**
     * Gets the preview text shown inside of a recycler view while editing the item.
     */
    default String preview() {
        return toString();
    }

    /**
     * Gets a drawable resource identifier representing the icon that describes
     * the type.
     */
    default int drawable() {
        return valueType().getDrawable();
    }

    /**
     * Gets the value as a {@link Map} object.
     * @throws ClassCastException if the value is not a {@link Map}
     */
    default Map<String, Value> getMap() {
        throw new ClassCastException("The value is not a Dictionary");
    }

    /**
     * Gets the value as an array of {@link Value} objects.
     * @throws ClassCastException if the value is not an array of {@link Value} objects.
     */
    default Value[] getArray() {
        throw new ClassCastException("The value is not an Array");
    }

    /**
     * Gets the value as a {@link String} object.
     * @throws ClassCastException if the value is not a {@link String}
     */
    default String getString() {
        throw new ClassCastException("The literal is not a String");
    }

    /**
     * Gets the value as a {@link Byte} object.
     * @throws ClassCastException if the value is not a {@link Byte}
     */
    default Byte getByte() {
        throw new ClassCastException("The literal is not a Byte");
    }

    /**
     * Gets the value as a {@link Float} object.
     * @throws ClassCastException if the value is not a {@link Float}
     */
    default Float getFloat() {
        throw new ClassCastException("The literal is not a Float");
    }

    /**
     * Gets the value as a {@link Double} object.
     * @throws ClassCastException if the value is not a {@link Double}
     */
    default Double getDouble() {
        throw new ClassCastException("The literal is not a Double");
    }

    /**
     * Gets the value as a {@link Integer} object.
     * @throws ClassCastException if the value is not a {@link Integer}
     */
    default Integer getInteger() {
        throw new ClassCastException("The literal is not an Integer");
    }

    /**
     * Gets the value as a {@link Long} object.
     * @throws ClassCastException if the value is not a {@link Long}
     */
    default Long getLong() {
        throw new ClassCastException("The literal is not a Long");
    }

    /**
     * Gets the value as a {@link Boolean} object.
     * @throws ClassCastException if the value is not a {@link Boolean}
     */
    default Boolean getBoolean() {
        throw new ClassCastException("The literal is not a Boolean");
    }
}
