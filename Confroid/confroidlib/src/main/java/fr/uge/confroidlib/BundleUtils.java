package fr.uge.confroidlib;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import androidx.annotation.NonNull;
import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.CustomizableView;
import fr.uge.confroidlib.annotations.CustomizedView;
import fr.uge.confroidlib.annotations.Description;
import fr.uge.confroidlib.annotations.GeoCoordinates;
import fr.uge.confroidlib.annotations.PhoneNumber;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.annotations.RegexValidator;

public class BundleUtils {
    public static final String ID_KEYWORD = "confroid#id";
    public static final String CLASS_KEYWORD = "confroid#class";
    public static final String REF_KEYWORD = "confroid#ref";
    public static final String PRIMITIVE_KEYWORD = "confroid#primitive";

    public static final String ANNOTATION_SEP = "@confroid#annotation@";
    public static final String ANNOTATION_PARAM = "param";

    /**
     * Returns the list of Confroid keywords
     *
     * @return The list of Confroid keywords
     */
    public static Set<String> confroidKeywords() {
        Set<String> keywords = new HashSet<>();
        keywords.add(ID_KEYWORD);
        keywords.add(CLASS_KEYWORD);
        keywords.add(REF_KEYWORD);
        keywords.add(PRIMITIVE_KEYWORD);
        keywords.add(ANNOTATION_SEP);

        return keywords;
    }

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
     * Converts this byte array to Byte Array
     *
     * @param array A byte array
     * @return A Byte array
     */
    private static Byte[] boxArray(byte[] array) {
        Byte[] Bytes = new Byte[array.length];

        for (int i = 0; i < array.length; i++) {
            Bytes[i] = array[i];
        }

        return Bytes;
    }

    /**
     * Converts this char array to Char Array
     *
     * @param array A char array
     * @return A Char array
     */
    private static Character[] boxArray(char[] array) {
        Character[] Chars = new Character[array.length];

        for (int i = 0; i < array.length; i++) {
            Chars[i] = array[i];
        }

        return Chars;
    }

    /**
     * Converts this short array to Short Array
     *
     * @param array A short array
     * @return A Short array
     */
    private static Short[] boxArray(short[] array) {
        Short[] Shorts = new Short[array.length];

        for (int i = 0; i < array.length; i++) {
            Shorts[i] = array[i];
        }

        return Shorts;
    }

    /**
     * Converts this int array to Integer Array
     *
     * @param array An int array
     * @return An Integer array
     */
    private static Integer[] boxArray(int[] array) {
        return IntStream.of(array).boxed().toArray(Integer[]::new);
    }

    /**
     * Converts this long array to Long Array
     *
     * @param array A long array
     * @return A Long array
     */
    private static Long[] boxArray(long[] array) {
        return LongStream.of(array).boxed().toArray(Long[]::new);
    }

    /**
     * Converts this float array to Float Array
     *
     * @param array A float array
     * @return A Float array
     */
    private static Float[] boxArray(float[] array) {
        Float[] Floats = new Float[array.length];

        for (int i = 0; i < array.length; i++) {
            Floats[i] = array[i];
        }

        return Floats;
    }

    /**
     * Converts this double array to Double Array
     *
     * @param array A double array
     * @return A Double array
     */
    private static Double[] boxArray(double[] array) {
        return DoubleStream.of(array).boxed().toArray(Double[]::new);
    }

    /**
     * Converts this boolean array to Boolean Array
     *
     * @param array A boolean array
     * @return A Boolean array
     */
    private static Boolean[] boxArray(boolean[] array) {
        Boolean[] Booleans = new Boolean[array.length];

        for (int i = 0; i < array.length; i++) {
            Booleans[i] = array[i];
        }

        return Booleans;
    }

    /**
     * Converts the given array of primitives to a new array with boxed values
     *
     * @param array A array of primitives values
     * @return A new array with boxed values.
     */
    private static Object[] getBoxedArray(Object array) {
        if (byte[].class.isAssignableFrom(array.getClass())) {
            return boxArray((byte[]) array);
        } else if (char[].class.isAssignableFrom(array.getClass())) {
            return boxArray((char[]) array);
        } else if (short[].class.isAssignableFrom(array.getClass())) {
            return boxArray((short[]) array);
        } else if (int[].class.isAssignableFrom(array.getClass())) {
            return boxArray((int[]) array);
        } else if (long[].class.isAssignableFrom(array.getClass())) {
            return boxArray((long[]) array);
        } else if (float[].class.isAssignableFrom(array.getClass())) {
            return boxArray((float[]) array);
        } else if (double[].class.isAssignableFrom(array.getClass())) {
            return boxArray((double[]) array);
        } else if (boolean[].class.isAssignableFrom(array.getClass())) {
            return boxArray((boolean[]) array);
        } else {
            return (Object[]) array;
        }
    }

    /**
     * Determines if the given object is boxing a primitive value + String.
     * It includes :
     * Byte, Short, Integer, Long, Float, Double, Boolean, Character and String.
     *
     * @param obj The object to determine if it's boxing a primitive value.
     * @return True if `obj` is boxing a primitive value (including String).
     * False otherwise
     */
    private static boolean isPrimitive(Object obj) {
        return obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Double ||
                obj instanceof Boolean ||
                obj instanceof Character ||
                obj instanceof String;
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

        if (isPrimitive(obj)) {
            addValueToBundle(bundle, PRIMITIVE_KEYWORD, obj);
            return bundle;
        }

        int refId = references.size() + 1;
        references.put(obj, refId); // Adding the object to references
        bundle.putInt(ID_KEYWORD, refId);
        bundle.putString(CLASS_KEYWORD, obj.getClass().getName());

        // Special treatment for Map, List and Array
        {
            if (Map.class.isAssignableFrom(obj.getClass())) {
                Bundle map = convertToBundle((Map<String, Object>) obj, true, references);
                map.remove(CLASS_KEYWORD);
                bundle.putAll(map);
            } else if (List.class.isAssignableFrom(obj.getClass())) {
                Bundle lst = convertToBundle((List<Object>) obj, true, references);
                lst.remove(CLASS_KEYWORD);
                bundle.putAll(lst);
            } else if (obj.getClass().isArray()) {
                Bundle array = convertToBundle(getBoxedArray(obj), true, references);
                array.remove(CLASS_KEYWORD);
                bundle.putAll(array);
            }
        }

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
        Map<Object, Map<Field, Integer>> remainingObjects = new IdentityHashMap<>();
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

        if (bundle.containsKey(PRIMITIVE_KEYWORD)) {
            return bundle.get(PRIMITIVE_KEYWORD);
        }

        if (!bundle.containsKey(CLASS_KEYWORD)) {
            throw new IllegalArgumentException(String.format("The bundle (and sub-bundles) must contains key %s or key %s", REF_KEYWORD, CLASS_KEYWORD));
        }

        Class<?> clazz = Class.forName(bundle.getString(CLASS_KEYWORD));

        // Special treatment for Map, List and Array
        {
            if (Map.class.isAssignableFrom(clazz)) {
                return getMap(bundle, parentObject, parentField, references, remainingObjects);
            } else if (List.class.isAssignableFrom(clazz)) {
                return getList(bundle, parentObject, parentField, references, remainingObjects);
            } else if (clazz.isArray()) {
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
     * A bundle represents an array if all of the keys are a succession of integers starting from 0,
     * ignoring keys which are Confroid-specific.
     *
     * @param bundle The bundle to test
     * @return True if the bundle represents an array. False otherwise
     */
    public static boolean isBundleArray(Bundle bundle) {
        SortedSet<String> set = new TreeSet<>(Comparator.comparing(Integer::valueOf));

        try {
            set.addAll(bundle.keySet().stream().
                    filter(key -> !(key.equals(BundleUtils.CLASS_KEYWORD) || key.equals(BundleUtils.ID_KEYWORD)))
                    .collect(Collectors.toSet()));
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
     * Assuming that the given bundle is representing a Map<String, Object>,
     * return the map object, recursively.
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
            if (!key.equals(CLASS_KEYWORD) && !key.equals(ID_KEYWORD)) {
                Object value = bundle.get(key);
                if (value instanceof Bundle) {
                    value = convertFromBundleAux(bundle.getBundle(key), parentObject, parentField, references, remainingObjects);
                }

                map.put(key, value);
            }
        }

        return map;
    }

    /**
     * Assuming that the given bundle is representing a List<Object>,
     * return the list object, recursively.
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

        /* Removing Confroid keywords to keep list indexes only.
        * Then sorting the indexes to add elements in order in the list. */
        List<String> bundleIndexes = bundle.keySet().stream()
                .filter(key -> !key.equals(CLASS_KEYWORD) && !key.equals(ID_KEYWORD))
                .sorted().collect(Collectors.toList());

        for (int i = 0; i < bundleIndexes.size(); i++) {
            Object value = bundle.get(bundleIndexes.get(i));
            if (value instanceof Bundle) {
                value = convertFromBundleAux(bundle.getBundle(String.valueOf(i)), parentObject, parentField, references, remainingObjects);
            }

            lst.add(value);
        }

        return lst;
    }

    /**
     * Assuming that the given bundle is representing an Object[],
     * return the array of object, recursively.
     *
     * @param bundle The bundle to convert into an array
     * @return The array where the type is given in the bundle with the key {@value #CLASS_KEYWORD}
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws NoSuchFieldException if a field with the specified name is not found.
     */
    private static Object getArray(Bundle bundle, Object parentObject, Field parentField, Map<Integer, Object> references, Map<Object, Map<Field, Integer>> remainingObjects) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        /* Removing Confroid keywords to keep list indexes only.
         * Then sorting the indexes to add elements in order in the array. */
        List<String> bundleIndexes = bundle.keySet().stream()
                .filter(key -> !key.equals(CLASS_KEYWORD) && !key.equals(ID_KEYWORD))
                .sorted().collect(Collectors.toList());

        Class<?> clazz = Class.forName(bundle.getString(CLASS_KEYWORD)).getComponentType();
        Object array = Array.newInstance(clazz, bundleIndexes.size());

        for (int i = 0; i < bundleIndexes.size(); i++) {
            Object value = bundle.get(bundleIndexes.get(i));
            if (value instanceof Bundle) {
                value = convertFromBundleAux(bundle.getBundle(String.valueOf(i)), parentObject, parentField, references, remainingObjects);
            }

            Array.set(array, i, value);
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
        } else if (annotation instanceof RangeValidator) {
            annotBundle.putLong(ANNOTATION_PARAM + "1", ((RangeValidator)annotation).minRange());
            annotBundle.putLong(ANNOTATION_PARAM + "2", ((RangeValidator)annotation).maxRange());
        } else if (annotation instanceof ClassValidator) {
            annotBundle.putString(ANNOTATION_PARAM + "1", ((ClassValidator)annotation).predicateClass().getSimpleName());
        } else if (annotation instanceof Description) {
            annotBundle.putString(ANNOTATION_PARAM + "1", ((Description)annotation).description().isEmpty() ?
                    fieldName : ((Description)annotation).description());
        } else if (annotation instanceof GeoCoordinates) {
            // Parameterless annotation
        } else if (annotation instanceof PhoneNumber) {
            // Parameterless annotation
        } else if (annotation instanceof CustomizedView) {
            annotBundle.putString(ANNOTATION_PARAM + "1", ((CustomizedView)annotation).viewClass().getSimpleName());
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

        Bundle params = bundle.getBundle(annotKey.get());
        if (annotName.equals(RegexValidator.class.getSimpleName())) {
            return new RegexValidator() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RegexValidator.class;
                }

                @Override
                public String pattern() {
                    return params.getString(ANNOTATION_PARAM + "1");
                }
            };
        }

        if (annotName.equals(RangeValidator.class.getSimpleName())) {
            return new RangeValidator() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RangeValidator.class;
                }

                @Override
                public long minRange() {
                    return params.getLong(ANNOTATION_PARAM + "1");
                }

                @Override
                public long maxRange() {
                    return params.getLong(ANNOTATION_PARAM + "2");
                }
            };
        }

        if (annotName.equals(ClassValidator.class.getSimpleName())) {
            return new ClassValidator() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return ClassValidator.class;
                }

                @Override
                public Class<? extends Predicate> predicateClass() {
                    try {
                        return (Class<? extends Predicate>) Class.forName("fr.uge.confroidlib.validators." + params.getString(ANNOTATION_PARAM + "1"));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }
            };
        }

        if (annotName.equals(Description.class.getSimpleName())) {
            return new Description() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Description.class;
                }

                @Override
                public String description() {
                    return params.getString(ANNOTATION_PARAM + "1");
                }
            };
        }

        if (annotName.equals(GeoCoordinates.class.getSimpleName())) {
            return new GeoCoordinates() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return GeoCoordinates.class;
                }
            };
        }

        if (annotName.equals(PhoneNumber.class.getSimpleName())) {
            return new PhoneNumber() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return PhoneNumber.class;
                }
            };
        }

        if (annotName.equals(CustomizedView.class.getSimpleName())) {
            return new CustomizedView() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return CustomizedView.class;
                }

                @Override
                public Class<? extends CustomizableView> viewClass() {
                    try {
                        return (Class<? extends CustomizableView>) Class.forName(params.getString(ANNOTATION_PARAM + "1"));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
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
