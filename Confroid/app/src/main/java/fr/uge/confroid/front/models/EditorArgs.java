package fr.uge.confroid.front.models;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import fr.uge.confroid.configuration.Value;

/**
 * Arguments to pass to an editor.
 */
public class EditorArgs {
    private final String name;
    private final Value value;
    private final List<Annotation> annotations;

    private EditorArgs(String name, Value value, List<Annotation> annotations) {
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
        this.annotations = Objects.requireNonNull(annotations);
    }

    /**
     * Gets the name of the editor page.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value that the editor should edit.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Gets an annotation from the annotations list of the value to edit.
     * @param clazz Class of the annotation to find.
     * @param <T> Runtime type of the annotation.
     * @return
     */
    public <T> Optional<T> getAnnotation(Class<?> clazz) {
        return annotations.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(e -> (T) e)
                .findFirst();
    }

    /**
     * Updates the value edited by the editor.
     * @param newValue New value.
     */
    public void setValue(Value newValue) {
        value.setValue(newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorArgs that = (EditorArgs) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public static EditorArgs create(EditorContext context, String key, Value value, List<Annotation> annotations) {
        return new EditorArgs(key, context.resolveReference(value), annotations);
    }

    public static EditorArgs create(EditorContext context, String key, Value value) {
        return new EditorArgs(key, context.resolveReference(value), new ArrayList<>());
    }
}

