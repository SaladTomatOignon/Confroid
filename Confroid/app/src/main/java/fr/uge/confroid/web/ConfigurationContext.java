package fr.uge.confroid.web;

class ConfigurationContext {
    private final String name;
    private final String version;
    private final String config;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getConfig() {
        return config;
    }

    public ConfigurationContext(String name, String version, String config) {
        this.name = name;
        this.version = version;
        this.config = config;
    }
}
