package fr.uge.confroid.utils;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Extensions {
    /**
     * Convert a map to a Bundle.
     * The key type must be String
     *
     * @param map The map to convert
     * @param <V> The type of the map value
     * @return A bundle containing the map entries
     * @throws IllegalArgumentException If the map contains value that is not supported
     */
    public static <V> Bundle convertToBundle(Map<String, V> map) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        for (String key : map.keySet()) {
            addValueToBundle(bundle, key, map.get(key));
        }

        return bundle;
    }

    /**
     * Convert a list to a Bundle, where the key is the index of the value.
     *
     * @param lst The list to convert
     * @param <V> The type of the list values
     * @return A bundle containing the list values, where the key is the index of the value
     * @throws IllegalArgumentException If the list contains value that is not supported
     */
    public static <V> Bundle convertToBundle(List<V> lst) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        for (int i = 0; i < lst.size(); i++) {
            addValueToBundle(bundle, String.valueOf(i), lst.get(i));
        }

        return bundle;
    }

    /**
     * Convert an array to a Bundle, where the key is the index of the value.
     *
     * @param array The array to convert
     * @param <V> The type of the array values
     * @return A bundle containing the array values, where the key is the index of the value
     * @throws IllegalArgumentException If the array contains value that is not supported
     */
    public static <V> Bundle convertToBundle(V[] array) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        for (int i = 0; i < array.length; i++) {
            addValueToBundle(bundle, String.valueOf(i), array[i]);
        }

        return bundle;
    }

    /**
     * Add an entry in the bundle, with the right value type.
     *
     * @param bundle The bundle in which insert the entry
     * @param key The key
     * @param value The value
     * @param <V> The type of the value
     * @throws IllegalArgumentException If the the value type is not supported of the value is a map
     * in which the key type is not a String
     */
    private static <V> void addValueToBundle(Bundle bundle, String key, V value) throws IllegalArgumentException {
        // TODO : Voir s'il y a plus jolie à faire
        if (value instanceof IBinder) {
            bundle.putBinder(key, (IBinder) value);
        } else if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof fr.uge.confroid.configuration.Byte) {
            bundle.putByte(key, ((fr.uge.confroid.configuration.Byte) value).getByte());
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        }  else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof fr.uge.confroid.configuration.String) {
            bundle.putString(key, ((fr.uge.confroid.configuration.String) value).getString());
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof fr.uge.confroid.configuration.Float) {
            bundle.putFloat(key, ((fr.uge.confroid.configuration.Float) value).getFloat());
        }  else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof fr.uge.confroid.configuration.Integer) {
            bundle.putInt(key, ((fr.uge.confroid.configuration.Integer) value).getInteger());
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof fr.uge.confroid.configuration.Boolean) {
            bundle.putBoolean(key, ((fr.uge.confroid.configuration.Boolean) value).getBoolean());
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof Map) {
            try {
                bundle.putBundle(key, convertToBundle((Map<String, Object>) value));
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("The map key type must be String", e);
            }
        }  else if (value instanceof fr.uge.confroid.configuration.Dictionary) {
            bundle.putBundle(key, convertToBundle(((fr.uge.confroid.configuration.Dictionary) value).getMap()));
        } else if (value instanceof List) {
            bundle.putBundle(key, convertToBundle((List<Object>) value));
        }  else if (value instanceof fr.uge.confroid.configuration.Array) {
            bundle.putBundle(key, convertToBundle(((fr.uge.confroid.configuration.Array) value).getArray()));
        } else {
            throw new IllegalArgumentException("Le type " + value.getClass() + " is not currently supported");
        }
    }
}
