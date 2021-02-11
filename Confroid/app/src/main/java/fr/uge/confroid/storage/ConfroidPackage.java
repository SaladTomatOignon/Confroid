package fr.uge.confroid.storage;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import fr.uge.confroid.configuration.Configuration;

@Entity( tableName = "packages", primaryKeys = { "name", "version" })
public class ConfroidPackage {

    @NonNull
    @ColumnInfo()
    private String name;

    @NonNull
    @ColumnInfo()
    private int version;

    @NonNull
    @ColumnInfo()
    private Date date;

    @NonNull
    @ColumnInfo()
    private Configuration config;

    @ColumnInfo()
    private String tag;

    public ConfroidPackage() {}

    @Ignore
    public ConfroidPackage(String name, int version, Date date, Configuration config, String tag) {
        this.name = Objects.requireNonNull(name);
        this.config = Objects.requireNonNull(config);
        this.date = Objects.requireNonNull(date);
        this.version = version;
        this.tag = tag;
    }

    @Ignore
    public ConfroidPackage(String name, int version, Date date, Configuration config) {
        this(name, version, date, config, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfroidPackage that = (ConfroidPackage) o;
        return version == that.version &&
                name.equals(that.name) &&
                date.equals(that.date) &&
                config.equals(that.config) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, date, config, tag);
    }

    @Override
    public String toString() {
        return "ConfroidPackage{" +
                "name='" + name + '\'' +
                ", version=" + version +
                ", date=" + date +
                ", config=" + config +
                ", tag='" + tag + '\'' +
                '}';
    }
}
