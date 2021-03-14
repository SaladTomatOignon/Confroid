package fr.uge.confroid.web.service.confroidstorageservice.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "Configurations")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "version"}),
        @UniqueConstraint(columnNames = {"name", "tag"})
})
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private int version;

    private String tag;

    @NotNull
    @Lob
    private String config;

    @NotNull
    private LocalDateTime creationDate;

    public Configuration() {

    }

    public Configuration(String name, int version, String tag, String config, LocalDateTime creationDate) {
        this.name = name;
        this.version = version;
        this.tag = tag;
        this.config = config;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return id == that.id &&
                version == that.version &&
                name.equals(that.name) &&
                Objects.equals(tag, that.tag) &&
                config.equals(that.config) &&
                creationDate.equals(that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, version, tag, config, creationDate);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", tag='" + tag + '\'' +
                ", config='" + config + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
