package fr.uge.confroid.dto;

import android.os.Bundle;

import java.util.Objects;

public class Configuration {
    private final String name;
    private final Bundle content;
    private final String tag;

    public Configuration(String name, Bundle content) {
        this(name, content, null);
    }

    public Configuration(String name, Bundle content, String tag) {
        this.name = Objects.requireNonNull(name);
        this.content = Objects.requireNonNull(content);
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public Bundle getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return name.equals(that.name) &&
                content.equals(that.content) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, tag);
    }
}
