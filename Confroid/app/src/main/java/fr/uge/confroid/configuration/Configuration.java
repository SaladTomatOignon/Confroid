package fr.uge.confroid.configuration;

import android.os.Bundle;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.confroidlib.BundleUtils;

public class Configuration {
    private final static java.lang.String PRIMITIVE_KEY_NAME = BundleUtils.PRIMITIVE_KEYWORD;
    private final static java.lang.String KEYWORD_SEP = ":";
    public final static java.lang.String PRIMITIVE_TYPE_KEYWORD = "confroid#type";

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

        int arrayIndex = 0;
        for (Value value : values) {
            if (value.isString() && (value.getString().startsWith(BundleUtils.ID_KEYWORD) || value.getString().startsWith(BundleUtils.CLASS_KEYWORD))) {
                bundle.putString(value.getString().split(KEYWORD_SEP)[0], value.getString().split(KEYWORD_SEP)[1]);
            } else {
                if (value.isPrimitive()) {
                    addPrimitiveToBundle(bundle, java.lang.String.valueOf(arrayIndex), value);
                } else {
                    bundle.putBundle(java.lang.String.valueOf(arrayIndex), valueToBundle(value));
                }
                arrayIndex++;
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
        } else if (value.isDouble()) {
            bundle.putDouble(key, value.getDouble());
        } else if (value.isInteger()) {
            bundle.putInt(key, value.getInteger());
        }  else if (value.isLong()) {
            bundle.putLong(key, value.getLong());
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

            int i = 0;
            for (java.lang.String key : keys.stream().sorted().collect(Collectors.toList())) {
                 if (key.equals(BundleUtils.ID_KEYWORD) || key.equals(BundleUtils.CLASS_KEYWORD)) {
                    values[i] = new String(key + KEYWORD_SEP + bundle.get(key));
                } else {
                    values[i] = convertObjectToValue(bundle.get(key));
                }
                i++;
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

    /**
     * Filters the given value to keep only non-Confroid properties.
     * If the value is an Array, the function returns a new Array containing the filtered values,
     * otherwise the function returns the same value object.
     *
     * @param value The value to filter
     * @return The value filtered
     */
    public static Value filterKeywords(Value value) {
        if (value.isMap()) {
            return new Dictionary(value.getMap().entrySet().stream().filter(entry ->
                    !BundleUtils.containsConfroidKeyword(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        } else if (value.isArray()) {
            return new Array((Value[]) Arrays.stream(value.getArray()).filter(
                    v -> !(v.isString() && BundleUtils.containsConfroidKeyword(v.getString()))
            ).toArray());
        }

        return value;
    }

    /**
     * Retrieves the Value node which corresponds to the ID given is parameter.
     * The complexity of this function is O(n).
     *
     * @param refId The value ID to retrieve
     * @param root The configuration root
     * @return The Value node with ID `refId`, or null if no Value node contains this ID.
     */
    public static Value getReferencedValue(int refId, Value root) {
        if (root.isMap()) {
            for (java.lang.String key : root.getMap().keySet()) {
                if (key.equals(BundleUtils.ID_KEYWORD) && root.getMap().get(key).getInteger().equals(refId)) {
                    return root;
                } else {
                    Value value = getReferencedValue(refId, root.getMap().get(key));
                    if (!Objects.isNull(value)) {
                        return value;
                    }
                }
            }
        } else if (root.isArray()) {
            for (Value v : root.getArray()) {
                if (v.isString() && v.getString().startsWith(BundleUtils.ID_KEYWORD) &&
                        java.lang.Integer.valueOf(v.getString().split(KEYWORD_SEP)[1]).equals(refId)) {
                    return root;
                } else {
                    Value value = getReferencedValue(refId, v);
                    if (!Objects.isNull(value)) {
                        return value;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns the difference between the both configurations, as a new configuration tree.
     * Each nodes are compared two by two.
     *
     * If a node of `configA` has a different type of `configB`,
     * then the chosen node is the one from `configB`.
     *
     * If an array or a dictionary contains more or less values than the other configuration,
     * the function returns the symmetric difference of the both arrays/dictionaries.
     *
     * @param configA The first configuration
     * @param configB The second configuration
     * @return The difference between the both configurations, or null if there is no differences.
     */
    public static Configuration difference(Configuration configA, Configuration configB) {
        return new Configuration(valueDifference(configA.content, configB.content));
    }

    /**
     * Returns the symmetric difference between the both values.
     * Null values are ignored.
     * If the primitive of `valueB` is different of `valueA`,
     * then the `valueB` is returned.
     *
     * @param valueA The first value
     * @param valueB The second value
     * @return The symmetric difference between theses 2 values, or null if there is no differences.
     */
    private static Value valueDifference(Value valueA, Value valueB) {
        if (valueA.equals(valueB)) {
            return null;
        }

        if (valueA.valueType() != valueB.valueType()) {
            return valueB;
        }

        if (valueA.isMap()) {
            return dictionaryDifference(valueA.getMap(), valueB.getMap());
        }

        if (valueA.isArray()) {
            return arrayDifference(valueA.getArray(), valueB.getArray());
        }

        return valueB;
    }

    /**
     * Returns the symmetric difference between the both dictionaries.
     * Null values are ignored.
     *
     * @param dicoA The first dictionary
     * @param dicoB The second dictionary
     * @return The symmetric difference between theses 2 dictionaries, or null if there is no differences.
     */
    private static Dictionary dictionaryDifference(Map<java.lang.String, Value> dicoA, Map<java.lang.String, Value> dicoB) {
        Map<java.lang.String, Value> map = new HashMap<>();

        Sets.symmetricDifference(dicoA.keySet(), dicoB.keySet()).forEach(key -> {
            if (dicoA.containsKey(key)) {
                map.put(key, dicoA.get(key));
            } else {
                map.put(key, dicoB.get(key));
            }
        });

        Sets.intersection(dicoA.keySet(), dicoB.keySet()).forEach(key -> {
            Value value = valueDifference(dicoA.get(key), dicoB.get(key));
            if (!Objects.isNull(value)) {
                map.put(key, value);
            }
        });

        if (map.isEmpty()) {
            return null;
        }

        return new Dictionary(map);
    }

    /**
     * Returns the symmetric difference between the both arrays.
     * Order is maintained and null values are ignored.
     *
     * @param arrayA The first array
     * @param arrayB The second array
     * @return The symmetric difference between theses 2 arrays, or null if there is no differences.
     */
    private static Array arrayDifference(Value[] arrayA, Value[] arrayB) {
        List<Value> values = new ArrayList<>();
        int sizeMax = Math.max(arrayA.length, arrayB.length);

        for (int i = 0; i < sizeMax; i++) {
            if (i >= arrayA.length) {
                values.add(arrayB[i]);
            } else if (i >= arrayB.length) {
                values.add(arrayA[i]);
            } else {
                Value value = valueDifference(arrayA[i], arrayB[i]);
                if (!Objects.isNull(value)) {
                    values.add(value);
                }
            }
        }

        if (values.isEmpty()) {
            return null;
        }

        Value[] valuesArray = new Value[values.size()];
        values.toArray(valuesArray);

        return new Array(valuesArray);
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
        } else if (object instanceof java.lang.Long) {
            return new Long((java.lang.Long) object);
        } else if (object instanceof java.lang.Float) {
            return new Float((java.lang.Float) object);
        } else if (object instanceof java.lang.Double) {
            return new Double((java.lang.Double) object);
        } else if (object instanceof java.lang.String) {
            return new String((java.lang.String) object);
        } else if (object instanceof Bundle) {
            return valueFromBundle((Bundle) object);
        } else {
            throw new IllegalArgumentException("The type " + object.getClass() + " is not currently supported");
        }

    }
}
