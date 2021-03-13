package fr.uge.confroid.configuration;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.confroidlib.BundleUtils;

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
        return valueToBundle(content);
    }

    /**
     * Fonction auxiliaire de {@link #toBundle()}
     *
     * @param value L'objet Value à convertir en bundle
     * @return Le Bundle créé à partir de l'objet Value
     */
    private Bundle valueToBundle(Value value) {
        if (value.isMap()) {
            return mapToBundle(value.getMap());
        } else if (value.isArray()) {
            return arrayToBundle(value.getArray());
        } else if (value.isPrimitive()) {
            Bundle bundle = new Bundle();
            addPrimitiveToBundle(bundle, PRIMITIVE_KEY_NAME, value);
            return bundle;
        } else {
            return null;
        }
    }

    /**
     * Convertis un dictionnaire dont les clés sont des String et les valeur des Value,
     * en Bundle
     *
     * @param map Le dictionnaire à convertir
     * @return Le Bundle créé à partir du dictionnaire
     */
    private Bundle mapToBundle(Map<java.lang.String, Value> map) {
        Bundle bundle = new Bundle();

        for (Map.Entry<java.lang.String, Value> key : map.entrySet()) {
            if (key.getValue().isPrimitive()) {
                addPrimitiveToBundle(bundle, key.getKey(), key.getValue());
            } else {
                bundle.putBundle(key.getKey(), valueToBundle(key.getValue()));
            }
        }

        return bundle;
    }

    /**
     * Convertis un tableau dont les clés représente l'indice de l'element dans le tableau,
     * et les valeurs sont des Value, en Bundle
     *
     * @param values Le tableau de Value à convertir
     * @return Le Bundle créé à partir du tableau
     */
    private Bundle arrayToBundle(Value[] values) {
        Bundle bundle = new Bundle();

        for (int i = 0; i < values.length; i++) {
            if (values[i].isPrimitive()) {
                addPrimitiveToBundle(bundle, java.lang.String.valueOf(i), values[i]);
            } else {
                bundle.putBundle(java.lang.String.valueOf(i), valueToBundle(values[i]));
            }
        }
        return bundle;
    }

    /**
     * Ajoute dans le bundle une primitive dont la clé est donnée en paramètre.
     *
     * @param bundle Le Bundle où il faut ajouter la primitive
     * @param key Le nom de clé à liée à la primitive
     * @param value La primitive à ajouter
     */
    private void addPrimitiveToBundle(Bundle bundle, java.lang.String key, Value value) {
        if (value.isBoolean()) {
            bundle.putBoolean(key, value.getBoolean());
        } else if (value.isByte()) {
            bundle.putByte(key, value.getByte());
        } else if (value.isFloat()) {
            bundle.putFloat(key, value.getFloat());
        } else if (value.isInteger()) {
            bundle.putInt(key, value.getInteger());
        } else if (value.isString()) {
            bundle.putString(key, value.getString());
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

        if (BundleUtils.isBundleArray(bundle)) { // If the bundle represents an array
            Value[] values = new Value[keys.size()];

            for (int i = 0; i < keys.size(); i++) {
                values[i] = convertObjectToValue(bundle.get(java.lang.String.valueOf(i)));
            }

            return new Array(values);
        } else if (keys.size() == 1 && bundle.containsKey(PRIMITIVE_KEY_NAME)) { // Or if it only contains a primitive
            Value value = convertObjectToValue(bundle.get(PRIMITIVE_KEY_NAME));
            if (value.isPrimitive()) {
                return value;
            }
        }

        // Else it's a Dictionary

        Map<java.lang.String, Value> map = new HashMap<>();

        for (java.lang.String key : keys) {
            map.put(key, convertObjectToValue(bundle.get(key)));
        }

        return new Dictionary(map);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public java.lang.String toString() {
        return Objects.toString(content);
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
