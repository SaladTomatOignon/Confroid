package fr.uge.confroid.web.service.confroidstorageservice.models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Configurations")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String version;
    private String config;

    public Configuration() {

    }

    public Configuration(String name, String version, String config) {
        this.name = name;
        this.version = version;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return name.equals(that.name) &&
                version.equals(that.version) &&
                config.equals(that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, config);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", config='" + config + '\'' +
                '}';
    }
}
