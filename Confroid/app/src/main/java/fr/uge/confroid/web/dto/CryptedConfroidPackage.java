package fr.uge.confroid.web.dto;

import java.util.Objects;

public class CryptedConfroidPackage {
    private String name;
    private int version;
    private String creationDate;
    private String config;
    private String tag;

    public CryptedConfroidPackage(String name, int version, String creationDate, String config, String tag) {
        this.name = name;
        this.version = version;
        this.creationDate = creationDate;
        this.config = config;
        this.tag = tag;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptedConfroidPackage that = (CryptedConfroidPackage) o;
        return version == that.version &&
                name.equals(that.name) &&
                creationDate.equals(that.creationDate) &&
                config.equals(that.config) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, creationDate, config, tag);
    }

    @Override
    public String toString() {
        return "CryptedConfigPackage{" +
                "name='" + name + '\'' +
                ", version=" + version +
                ", date=" + creationDate +
                ", cryptedConfig='" + config + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
