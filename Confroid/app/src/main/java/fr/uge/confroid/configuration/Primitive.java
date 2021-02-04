package fr.uge.confroid.configuration;

public interface Primitive extends Value {
    @Override
    default boolean isPrimitive() {
        return true;
    }

    @Override
    default Primitive getPrimitive() {
        return this;
    }

    default boolean isString() {
        return false;
    }

    default boolean isByte() {
        return false;
    }

    default boolean isFloat() {
        return false;
    }

    default boolean isInteger() {
        return false;
    }

    default boolean isBoolean() {
        return false;
    }

    default java.lang.String getString() {
        throw new ClassCastException("The literal is not a String");
    }

    default java.lang.Byte getByte() {
        throw new ClassCastException("The literal is not a Byte");
    }

    default java.lang.Float getFloat() {
        throw new ClassCastException("The literal is not a Float");
    }

    default java.lang.Integer getInteger() {
        throw new ClassCastException("The literal is not an Integer");
    }

    default java.lang.Boolean getBoolean() {
        throw new ClassCastException("The literal is not a Boolean");
    }

}
