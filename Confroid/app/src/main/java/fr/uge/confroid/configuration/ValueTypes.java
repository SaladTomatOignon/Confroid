package fr.uge.confroid.configuration;

/**
 * List of all supported value types.
 */
public enum ValueTypes {
    MAP(false),
    BYTE(true),
    ARRAY(false),
    FLOAT(true),
    STRING(true),
    BOOLEAN(true),
    INTEGER(true);

    private boolean primitive;

    ValueTypes(boolean primitive) {
        this.primitive = primitive;
    }

    /**
     * Gets a value indicating whether the value is a primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }
}
