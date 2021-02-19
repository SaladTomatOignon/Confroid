package fr.uge.confroidlib;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.uge.confroidlib.annotations.RegexValidator;

public class BundleUtils {
    public static final String ID_KEYWORD = "confroid#id";
    public static final String CLASS_KEYWORD = "confroid#class";
    public static final String REF_KEYWORD = "confroid#ref";

    private static final String ANNOTATION_SEP = "@";
    private static final String ANNOTATION_PARAM = "param";

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
        return convertToBundle(map, false, null);
    }

    /**
     * Convert a map to a Bundle.
     * The key type must be String
     *
     * @param map The map to convert
     * @param <V> The type of the map value
     * @param reflection If true, for each value of `map` if the value an object, retrieves
     *                   each field of the object using reflection (recursive).
     *                   Moreover, it adds a key {@value #CLASS_KEYWORD} with the type of the map
     * @param references A map containing unique reference to an object. Must be non null if `reflection` is True
     *                   Key : Reference to an object ; Value : Its unique ID
     * @return A bundle containing the map entries
     * @throws IllegalArgumentException If the map contains value that is not supported
     */
    private static <V> Bundle convertToBundle(Map<String, V> map, boolean reflection, Map<Object, Integer> references) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        if (reflection) {
            bundle.putString(CLASS_KEYWORD, map.getClass().getName());
        }

        for (String key : map.keySet()) {
            addValueToBundle(bundle, key, map.get(key), reflection, references);
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
        return convertToBundle(lst, false, null);
    }

    /**
     * Convert a list to a Bundle, where the key is the index of the value.
     *
     * @param lst The list to convert
     * @param <V> The type of the list values
     * @param reflection If true, for each value of `lst` if the value an object, retrieves
     *                   each field of the object using reflection (recursive).
     *                   Moreover, it adds a key {@value #CLASS_KEYWORD} with the type of the list
     * @param references A map containing unique reference to an object. Must be non null if `reflection` is True
     *                   Key : Reference to an object ; Value : Its unique ID
     * @return A bundle containing the list values, where the key is the index of the value
     * @throws IllegalArgumentException If the list contains value that is not supported
     */
    private static <V> Bundle convertToBundle(List<V> lst, boolean reflection, Map<Object, Integer> references) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        if (reflection) {
            bundle.putString(CLASS_KEYWORD, lst.getClass().getName());
        }

        for (int i = 0; i < lst.size(); i++) {
            addValueToBundle(bundle, String.valueOf(i), lst.get(i), reflection, references);
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
        return convertToBundle(array, false, null);
    }

    /**
     * Convert an array to a Bundle, where the key is the index of the value.
     *
     * @param array The array to convert
     * @param <V> The type of the array values
     * @param reflection If true, for each value of `array` if the value an object, retrieves
     *                   each field of the object using reflection (recursive).
     *                   Moreover, it adds a key {@value #CLASS_KEYWORD} with the type of the array
     * @param references A map containing unique reference to an object. Must be non null if `reflection` is True
     *                   Key : Reference to an object ; Value : Its unique ID
     * @return A bundle containing the array values, where the key is the index of the value
     * @throws IllegalArgumentException If the array contains value that is not supported
     */
    private static <V> Bundle convertToBundle(V[] array, boolean reflection, Map<Object, Integer> references) throws IllegalArgumentException {
        Bundle bundle = new Bundle();

        if (reflection) {
            bundle.putString(CLASS_KEYWORD, array.getClass().getName());
        }

        for (int i = 0; i < array.length; i++) {
            addValueToBundle(bundle, String.valueOf(i), array[i], reflection, references);
        }

        return bundle;
    }

    /**
     * Convert an object to a Bundle using reflexion.
     * Each key of the bundle represents the name of a field of the object.
     * The bundle contains the class name ({@value #CLASS_KEYWORD}) of the object.
     * It also contains an unique id ({@value #ID_KEYWORD}) for an unique instance of a potential nested object,
     * to avoid duplicates.
     * So if a key has for name {@value #REF_KEYWORD} and an id for value, then the id refers to an already existing object.
     *
     * This function is recursive.
     * The object and each nested object must have a default constructor (without argument).
     *
     * @param obj The object to convert into Bundle
     * @return A Bundle containing every fields of the object (recursively)
     */
    public static Bundle convertToBundleReflection(Object obj) {
        return convertToBundleReflectionAux(obj, new IdentityHashMap<>());
    }

    /**
     * Auxiliary function of {@link #convertToBundleReflection(Object)}
     *
     * @param obj The object to convert into Bundle
     * @param references A map containing unique reference to an object.
     *                   Key : Reference to an object ; Value : Its unique ID
     * @return A Bundle containing every fields of the object (recursively)
     */
    private static Bundle convertToBundleReflectionAux(Object obj, Map<Object, Integer> references) {
        Bundle bundle = new Bundle();

        if (references.containsKey(obj)) { // We don't duplicate the object
            bundle.putInt(REF_KEYWORD, references.get(obj));
            return bundle;
        }

        int refId = references.size() + 1;
        references.put(obj, refId); // Adding the object to references
        bundle.putInt(ID_KEYWORD, refId);
        bundle.putString(CLASS_KEYWORD, obj.getClass().getName());

        for (Field field : obj.getClass().getFields()) {
            try {
                addValueToBundle(bundle, field.getName(), field.get(obj), true, references);
            } catch (IllegalAccessException e) {
                continue;
            }

            // Retrieving field's annotations
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                addAnnotationToBundle(annotation, bundle, field.getName());
            }
        }

        return bundle;
    }

    /**
     * Convert the given bundle in an object, where the class type is given in the bundle.
     * For each bundle and sub-bundle, it must contains :
     * - The key {@value #CLASS_KEYWORD} where the value is the class name of the object to be converted
     * - The key {@value #ID_KEYWORD} where the value is an unique identifier for an instance of class.
     *   (Only if the object value is not a Map, a List, or an Array)
     * - OR the key {@value #REF_KEYWORD} where the value is the id of another bundle / sub-bundle
     *
     * Every other key is considered to be a field of the object.
     * The object to which the bundle should be created must have a constructor without argument and no final fields.
     *
     * @param bundle The bundle to convert into an object
     * @return The object converted, where the type is given by the key {@value #CLASS_KEYWORD}
     * @throws IllegalArgumentException if the bundle does not contain key {@value #CLASS_KEYWORD} or key {@value #REF_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    public static Object convertFromBundle(Bundle bundle) throws IllegalArgumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Map<Integer, Object> references = new IdentityHashMap<>();
        Map<Object, Map<Field, Integer>> remainingObjects = new HashMap<>();
        Object obj = convertFromBundleAux(bundle, null, null, references, remainingObjects);

        // Now we have read all of the ids, we can map the remaining objects
        for (Object parentObj : remainingObjects.keySet()) {
            for (Field field : remainingObjects.get(parentObj).keySet()) {
                field.set(parentObj, references.get(remainingObjects.get(parentObj).get(field)));
            }
        }

        return obj;
    }

    /**
     * Auxiliary function of {@link #convertFromBundle(Bundle)}.
     *
     * @param bundle The bundle to convert into an object
     * @param parentObject The object supposed to contain the bundle, or null if there is not
     * @param parentField The associated field supposed to contain the bundle, null if there is no parent object
     * @param references A map containing unique reference to an object.
     *                   Key : Reference to an object ; Value : Its unique ID
     * @param remainingObjects Referenced objects that could not be loaded, because the original object has not been read
     * @return The object converted, where the type is given by the key {@value #CLASS_KEYWORD}
     * @throws IllegalArgumentException if the bundle does not contain key {@value #CLASS_KEYWORD} or key {@value #REF_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    private static Object convertFromBundleAux(Bundle bundle, Object parentObject, Field parentField, Map<Integer, Object> references, Map<Object, Map<Field, Integer>> remainingObjects) throws IllegalArgumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // Referenced object : if the original object is not yet retrieved,
        // then we save the parent object with its associated field and the id in which the object has to be put
        if (bundle.containsKey(REF_KEYWORD)) {
            if (references.containsKey(bundle.getInt(REF_KEYWORD))) {
                return references.get(bundle.getInt(REF_KEYWORD));
            }

            if (!remainingObjects.containsKey(parentObject)) {
                remainingObjects.put(parentObject, new HashMap<>());
            }
            remainingObjects.get(parentObject).put(parentField, bundle.getInt(REF_KEYWORD));

            return null;
        }

        if (!bundle.containsKey(CLASS_KEYWORD)) {
            throw new IllegalArgumentException(String.format("The bundle (and sub-bundles) must contains key %s or key %s", REF_KEYWORD, CLASS_KEYWORD));
        }

        Class clazz = Class.forName(bundle.getString(CLASS_KEYWORD));

        // Special treatment for Map, List and Array
        {
            if (Map.class.isAssignableFrom(clazz)) {
                return getMap(bundle, parentObject, parentField, references, remainingObjects);
            } else if (List.class.isAssignableFrom(clazz)) {
                return getList(bundle, parentObject, parentField, references, remainingObjects);
            } else if (Object[].class.isAssignableFrom(clazz)) {
                return getArray(bundle, parentObject, parentField, references, remainingObjects);
            }
        }

        // Instantiating the object
        Object obj = clazz.newInstance();

        // Register the id for the object
        if (!bundle.containsKey(ID_KEYWORD)) {
            throw new IllegalArgumentException(String.format("The bundle (or sub-bundles) must contains key %s since the value is not a Map, List or Array", ID_KEYWORD));
        }
        references.put(bundle.getInt(ID_KEYWORD), obj);

        // Fields assignment
        for (String key : bundle.keySet()) {
            if (!key.equals(CLASS_KEYWORD) && !key.equals(ID_KEYWORD) && !key.contains(ANNOTATION_SEP)) {
                Field field = clazz.getField(key);
                Object value = bundle.get(key) instanceof Bundle ? convertFromBundleAux(bundle.getBundle(key), obj, field, references, remainingObjects) : bundle.get(key);
                field.set(obj, value);
            }
        }

        return obj;
    }

    /**
     * Determines if the bundle represents an array.
     * A bundle represents an array if all of the keys are a succession of integers starting from 0.
     *
     * @param bundle The bundle to test
     * @return True if the bundle represents an array. False otherwise
     */
    public static boolean isBundleArray(Bundle bundle) {
        SortedSet<String> set = new TreeSet<>(Comparator.comparing(Integer::valueOf));

        try {
            set.addAll(bundle.keySet());
        } catch (IllegalArgumentException e) { // If one element is not an int.
            return false;
        }

        return IntStream.range(0, set.size()).boxed().map(String::valueOf).collect(Collectors.toSet()).equals(set);
    }

    /**
     * Add an entry in the bundle, with the right value type.
     *
     * @param bundle The bundle in which insert the entry
     * @param key The key
     * @param value The value
     * @param <V> The type of the value
     * @throws IllegalArgumentException If the value type is not supported or if the value is a map
     * in which the key is not of type String
     */
    private static <V> void addValueToBundle(Bundle bundle, String key, V value) throws IllegalArgumentException {
        addValueToBundle(bundle, key, value, false, null);
    }

    /**
     * Assuming that the given bundle is representing a Map<String, Object>, where the value is
     * itself represented by a bundle, return the map object, recursively.
     *
     * @param bundle The bundle to convert into a map
     * @return The map where the type is given in the bundle with the key {@value #CLASS_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    private static Map<String, Object> getMap(Bundle bundle, Object parentObject, Field parentField, Map<Integer, Object> references, Map<Object, Map<Field, Integer>> remainingObjects) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Map<String, Object> map = null;

        try {
            map = (Map<String, Object>) Class.forName(bundle.getString(CLASS_KEYWORD)).newInstance();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The map key must be of type String", e);
        }

        for (String key : bundle.keySet()) {
            if (!key.equals(CLASS_KEYWORD)) {
                map.put(key, convertFromBundleAux(bundle.getBundle(key), parentObject, parentField, references, remainingObjects));
            }
        }

        return map;
    }

    /**
     * Assuming that the given bundle is representing a List<Object>, where the value is
     * itself represented by a bundle, return the list object, recursively.
     *
     * @param bundle The bundle to convert into a List
     * @return The list where the type is given in the bundle with the key {@value #CLASS_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    private static List<Object> getList(Bundle bundle, Object parentObject, Field parentField, Map<Integer, Object> references, Map<Object, Map<Field, Integer>> remainingObjects) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<Object> lst = (List<Object>) Class.forName(bundle.getString(CLASS_KEYWORD)).newInstance();

        for (int i = 0; i < bundle.size() - 1; i++) {
            lst.add(convertFromBundleAux(bundle.getBundle(String.valueOf(i)), parentObject, parentField, references, remainingObjects));
        }

        return lst;
    }

    /**
     * Assuming that the given bundle is representing an Object[], where the value is
     * itself represented by a bundle, return the array of object, recursively.
     *
     * @param bundle The bundle to convert into an array
     * @return The array where the type is given in the bundle with the key {@value #CLASS_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    private static Object[] getArray(Bundle bundle, Object parentObject, Field parentField, Map<Integer, Object> references, Map<Object, Map<Field, Integer>> remainingObjects) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object[] array = (Object[]) Class.forName(bundle.getString(CLASS_KEYWORD)).newInstance();

        for (int i = 0; i < bundle.size() - 1; i++) {
            array[i] = convertFromBundleAux(bundle.getBundle(String.valueOf(i)), parentObject, parentField, references, remainingObjects);
        }

        return array;
    }

    /**
     * If the annotation is a ConfroidAnnotation, then the function adds a new Bundle in the given bundle
     * where the key = "fieldName" + {@value #ANNOTATION_SEP} + "annotationName".
     *
     * This new nested bundle is filled with the unapply() method of the annotation which adds
     * an entry for each parameter of the annotation.
     *
     * @param annotation The annotation to add to bundle
     * @param bundle The bundle to fill
     * @param fieldName The field associated to the annotation
     */
    private static void addAnnotationToBundle(Annotation annotation, Bundle bundle, String fieldName) {
        Bundle annotBundle = new Bundle();

        if (annotation instanceof RegexValidator) {
            annotBundle.putString(ANNOTATION_PARAM + "1", ((RegexValidator)annotation).pattern());
        } else {
            return;
        }

        bundle.putBundle(fieldName + ANNOTATION_SEP + annotation.annotationType().getSimpleName(), annotBundle);
    }

    /**
     * Retrieves the annotation corresponding to the given Bundle.
     * The unique key of the bundle must be of form "fieldName" + {@value #ANNOTATION_SEP} + "annotationName"
     * and its value must be a bundle containing the information of the annotation.
     *
     * @param bundle The bundle containing the annotation information
     * @return The annotation
     */
    public static Annotation getAnnotationFromBundle(Bundle bundle) {
        Optional<String> annotKey = bundle.keySet().stream().findFirst();
        String annotName;

        try {
            annotName = annotKey.get().split(ANNOTATION_SEP)[1];
        } catch (Exception e) {
            throw new IllegalArgumentException("The bundle must contain the key corresponding to an annotation.\n" +
                    "It must be of the form 'fieldName" + ANNOTATION_SEP + "annotationName'");
        }

        if (annotName.equals(RegexValidator.class.getSimpleName())) {
            return new RegexValidator() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RegexValidator.class;
                }

                @Override
                public String pattern() {
                    return bundle.getString(ANNOTATION_PARAM + "1");
                }
            };
        }

        return null;
    }

    /**
     * Add an entry in the bundle, with the right value type.
     *
     * @param bundle The bundle in which insert the entry
     * @param key The key
     * @param value The value
     * @param <V> The type of the value
     * @param reflection If true and if `value` is not a primitive, add a bundle as value which contains
     *           each field of the object using reflection (recursive)
     * @param references A map containing unique reference to an object. Must be non null if `reflection` is True
     *                   Key : Reference to an object ; Value : Its unique ID
     * @throws IllegalArgumentException If the value type is not supported or if the value is a map
     * in which the key is not of type String
     */
    private static <V> void addValueToBundle(Bundle bundle, String key, V value, boolean reflection, Map<Object, Integer> references) throws IllegalArgumentException {
        // TODO : Voir s'il y a plus jolie à faire
        if (value instanceof IBinder) {
            bundle.putBinder(key, (IBinder) value);
        } else if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        }  else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        }  else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof Parcelable && !reflection) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable && !reflection) {
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof Map) {
            try {
                bundle.putBundle(key, convertToBundle((Map<String, Object>) value, reflection, references));
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("The map key must be of type String", e);
            }
        } else if (value instanceof List) {
            bundle.putBundle(key, convertToBundle((List<Object>) value, reflection, references));
        } else if (value instanceof Object[]) {
            bundle.putBundle(key, convertToBundle((Object[]) value, reflection, references));
        } else {
            if (reflection) {
                bundle.putBundle(key, convertToBundleReflectionAux(value, references));
            } else {
                throw new IllegalArgumentException("The type " + value.getClass() + " is not currently supported");
            }
        }
    }
}
