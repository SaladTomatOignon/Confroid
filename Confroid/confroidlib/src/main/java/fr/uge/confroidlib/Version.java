package fr.uge.confroidlib;

import java.util.Date;
import java.util.Objects;

public class Version {
    private Date date;
    private String tag;
    private String name;

    public Version(String name, Date date, String tag) {
        this.name = Objects.requireNonNull(name);
        this.date = Objects.requireNonNull(date);
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(date, version.date) &&
                Objects.equals(tag, version.tag) &&
                Objects.equals(name, version.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, tag, name);
    }

    @Override
    public String toString() {
        return "Version{" +
                "date=" + date +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
