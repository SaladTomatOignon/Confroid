package fr.uge.confroid.configuration;

import fr.uge.confroid.R;

/**
 * List of all supported value types.
 */
public enum ValueTypes {
    MAP(false, R.drawable.ic_type_object),
    ARRAY(false, R.drawable.ic_type_array),

    STRING(true, R.drawable.ic_type_text),
    BOOLEAN(true, R.drawable.ic_type_boolean),

    BYTE(true, R.drawable.ic_type_number),
    FLOAT(true, R.drawable.ic_type_number),
    DOUBLE(true, R.drawable.ic_type_number),
    INTEGER(true, R.drawable.ic_type_number),
    LONG(true, R.drawable.ic_type_number);

    private final boolean primitive;
    private final int drawable;

    ValueTypes(boolean primitive, int drawable) {
        this.primitive = primitive;
        this.drawable = drawable;
    }

    /**
     * Gets a drawable resource identifier representing the icon that describes
     * the type.
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Gets a value indicating whether the value is a primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }
}
