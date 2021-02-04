package fr.uge.confroid.storage;

import fr.uge.confroid.configuration.Configuration;

public class ConfroidPackage {

    private String name;
    private String version;
    private Configuration config;
    private String tag;

    public ConfroidPackage(String name , String version, Configuration config, String tag) {
        this.name = name;
        this.version = version;
        this.config = config;
        this.tag = tag;
    }

    public String getName() {

        return name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
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
}
