package fr.uge.confroid.configuration;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.uge.confroid.storage.serialization.ConfigDeserializer;
import fr.uge.confroid.storage.serialization.ConfigSerializer;
import fr.uge.confroid.utils.Extensions;

public class Configuration {
    private final static java.lang.String PRIMITIVE_KEY_NAME = "primitive";

    private final Value content;

    public Configuration(Value content) {
        this.content = content;
    }

    public Value getContent() {
        return content;
    }

    /**
     * Crée une configuration depuis une chaine au format Json.
     *
     * @param json Le json à parser
     * @return La Configuration créée depuis le Json
     */
    public static Configuration fromJson(java.lang.String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigDeserializer()).create();
        return gson.fromJson(json, Configuration.class);
    }

    /**
     * Convertis cette configuration au format Json.
     *
     * @return La description Json de cette configuration
     */
    public java.lang.String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigSerializer()).create();
        return gson.toJson(this);
    }

    /**
     * Crée un Bundle à partir de cette configuration.
     * Pour un tableau on utilise comme clé de chaque entrée son indice dans le tableau.
     * Si cette configuration ne contient qu'une valeur primitive, l'unique clé du bundle aura pour valeur {@value #PRIMITIVE_KEY_NAME}.
     *
     * @return Le Bundle créé à partir de cette configuration
     */
    public Bundle toBundle() {
        if (content.isDictionary()) {
            return Extensions.convertToBundle(content.getMap());
        } else if (content.isArray()) {
            return Extensions.convertToBundle(content.getArray());
        } else if (content.isPrimitive()) {
            Bundle bundle = new Bundle();
            Primitive prim = content.getPrimitive();
            java.lang.String keyValue = PRIMITIVE_KEY_NAME;

            if (prim.isBoolean()) {
                bundle.putBoolean(keyValue, prim.getBoolean());
            } else if (prim.isByte()) {
                bundle.putByte(keyValue, prim.getByte());
            } else if (prim.isFloat()) {
                bundle.putFloat(keyValue, prim.getFloat());
            } else if (prim.isInteger()) {
                bundle.putInt(keyValue, prim.getInteger());
            } else if (prim.isString()) {
                bundle.putString(keyValue, prim.getString());
            } else {
                return null;
            }

            return bundle;
        } else {
            return null;
        }
    }

    /**
     * Crée une nouvelle configuration à partir du Bundle en paramètre.
     *
     * Si le bundle (ou un sous bundle) contient des clés dont toutes les valeurs sont une succession d'entiers démarrant à 0,
     * Alors la méthode renvoie un Array dont les valeurs sont celles associées aux clés dans l'ordre numérique croissant.
     *
     * Si le bundle (ou un sous bundle) contient une unique clé dont la valeur est {@value #PRIMITIVE_KEY_NAME}
     * et que la valeur associée à cette clé est évaluée comme du type Primitive,
     * Alors la méthode renvoie un Primitive.
     *
     * @param bundle Le Bundle à parser
     * @return La configuration créée depuis le Bundle
     * @throws IllegalArgumentException Si une valeur du bundle ne peut pas être parsée dans une configuration
     */
    public static Configuration fromBundle(Bundle bundle) throws IllegalArgumentException {
        return new Configuration(valueFromBundle(bundle));
    }

    /**
     * Fonction auxiliaire de {@link #fromBundle(Bundle)}.
     *
     * @param bundle Le Bundle à parser
     * @return L'objet Value créé depuis le Bundle, ou null si le bundle ne contient aucune valeur
     * @throws IllegalArgumentException Si une valeur du bundle ne peut pas être parsée dans une configuration
     */
    private static Value valueFromBundle(Bundle bundle) throws IllegalArgumentException {
        Set<java.lang.String> keys = bundle.keySet();

        if (keys.isEmpty()) {
            return null;
        }

        if (Extensions.isBundleArray(bundle)) { // If the bundle represents an array
            Value[] values = new Value[keys.size()];

            for (int i = 0; i < keys.size(); i++) {
                values[i] = convertObjectToValue(bundle.get(java.lang.String.valueOf(i)));
            }

            return new Array(values);
        } else if (keys.size() == 1 && bundle.containsKey(PRIMITIVE_KEY_NAME)) { // Or if it only contains a primitive
            Value value = convertObjectToValue(bundle.get(PRIMITIVE_KEY_NAME));
            if (value.isPrimitive()) {
                return value.getPrimitive();
            }
        }

        // Else it's a Dictionary

        Map<java.lang.String, Value> map = new HashMap<>();

        for (java.lang.String key : keys) {
            map.put(key, convertObjectToValue(bundle.get(key)));
        }

        return new Dictionary(map);
    }

    /**
     * Convert an object to a Value of Configuration if possible.
     *
     * @param object The object to convert to
     * @return The Value corresponding to the conversion result
     * @throws IllegalArgumentException If the object can not be converted to Value
     */
    private static Value convertObjectToValue(Object object) throws IllegalArgumentException {
        if (object instanceof java.lang.Boolean) {
            return new Boolean((java.lang.Boolean) object);
        } else if (object instanceof java.lang.Byte) {
            return new Byte((java.lang.Byte) object);
        } else if (object instanceof java.lang.Integer) {
            return new Integer((java.lang.Integer) object);
        } else if (object instanceof java.lang.Float) {
            return new Float((java.lang.Float) object);
        } else if (object instanceof java.lang.String) {
            return new String((java.lang.String) object);
        } else if (object instanceof Bundle) {
            return valueFromBundle((Bundle) object);
        } else {
            throw new IllegalArgumentException("The type " + object.getClass() + " is not currently supported");
        }
    }
}
