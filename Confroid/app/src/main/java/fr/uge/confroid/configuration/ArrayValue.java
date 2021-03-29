package fr.uge.confroid.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.uge.confroidlib.BundleUtils;

public class ArrayValue implements Value {
    private Value[] values;

    public ArrayValue(Value[] values) {
        this.values = values;
    }

    /**
     * Gets the values of this array without confroid keywords.
     * @return A list of the values.
     */
    public List<Value> editableEntries() {
        return Arrays.stream(values)
                .filter(v -> !(v.isString() &&
                        BundleUtils.confroidKeywords().stream().anyMatch(
                                keyword -> v.getString().contains(keyword))))
                .collect(Collectors.toList());
    }

    /**
     * Gets the values of this array containing confroid keywords.
     * @return A list of the values.
     */
    public List<Value> nonEditableEntries() {
        return Arrays.stream(values)
                .filter(v -> v.isString() &&
                        BundleUtils.confroidKeywords().stream().anyMatch(
                                keyword -> v.getString().contains(keyword)))
                .collect(Collectors.toList());
    }

    @Override
    public ValueTypes valueType() {
        return ValueTypes.ARRAY;
    }

    @Override
    public void setValue(Value value) {
        if (value.valueType() != valueType()) {
            throw new IllegalArgumentException("The given value must be of type " + valueType());
        }

        this.values = value.getArray();
    }

    @Override
    public Value[] getArray() {
        return values;
    }

    @Override
    public String preview() {
        int size = Configuration.filterKeywords(this).getArray().length;
        return size + " items";
    }

    @Override
    public ArrayValue deepCopy() {
        return new ArrayValue(
                Arrays.stream(values).map(Value::deepCopy)
                        .toArray(Value[]::new)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayValue array1 = (ArrayValue) o;
        return Arrays.equals(values, array1.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
