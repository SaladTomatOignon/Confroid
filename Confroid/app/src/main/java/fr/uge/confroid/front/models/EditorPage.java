package fr.uge.confroid.front.models;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import fr.uge.confroid.configuration.Value;

public class EditorPage {
    private final String name;
    private final Value value;
    private final List<Annotation> annotations;

    private EditorPage(String name, Value value, List<Annotation> annotations) {
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
        this.annotations = Objects.requireNonNull(annotations);
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public <T> Optional<T> getAnnotation(Class clazz) {
        return annotations.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(e -> (T) e)
                .findFirst();
    }

    public void setValue(Value newValue) {
        value.setValue(newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorPage that = (EditorPage) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public static EditorPage create(EditorContext editorContext, String key, Value value, List<Annotation> annotations) {
        return new EditorPage(key, editorContext.resolveReference(value), annotations);
    }

    public static EditorPage create(EditorContext editorContext, String key, Value value) {
        return new EditorPage(key, editorContext.resolveReference(value), new ArrayList<>());
    }
}

