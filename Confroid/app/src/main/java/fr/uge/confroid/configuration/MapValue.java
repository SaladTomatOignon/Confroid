package fr.uge.confroid.configuration;

import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.confroidlib.BundleUtils;

public class MapValue implements Value {
    private Map<String, Value> values;

    public MapValue(Map<String, Value> values) {
        this.values = Objects.requireNonNull(values);
    }

    public boolean isSubClassOfMap() {
        Value className = values.get(BundleUtils.CLASS_KEYWORD);
        if (className == null) {
            return false;
        }

        try {
            return Map.class.isAssignableFrom(Class.forName(className.getString()));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Gets the entries of the map without confroid keywords.
     * @return A set of the entries.
     */
    public Set<Map.Entry<String, Value>> editableEntries() {
        return values.entrySet().stream()
                .filter(entry -> BundleUtils.confroidKeywords().stream()
                        .noneMatch(keyword -> entry.getKey().contains(keyword)))
                .collect(Collectors.toSet());
    }

    public List<Annotation> annotations(String key) {
        return values.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(key + BundleUtils.ANNOTATION_SEP))
                .map(entry -> {
                    Bundle bundle = new Bundle();
                    bundle.putBundle(entry.getKey(), new Configuration(entry.getValue()).toBundle());
                    return BundleUtils.getAnnotationFromBundle(bundle);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.MAP;
    }


    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.values = value.getMap();
    }

    @Override
    public Map<String, Value> getMap() {
        return values;
    }

    @Override
    public String preview() {
        int size = Configuration.filterKeywords(this).getMap().size();
        return size + " keys";
    }

    @Override
    public MapValue deepCopy() {
        return new MapValue(
                values.entrySet().stream()
                        .map(entry ->
                                new AbstractMap.SimpleEntry<>(
                                        entry.getKey(),
                                        entry.getValue().deepCopy()))
                        .collect(Collectors.toMap(
                                AbstractMap.SimpleEntry::getKey,
                                AbstractMap.SimpleEntry::getValue)
                        )
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapValue that = (MapValue) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return Objects.toString(values);
    }
}
