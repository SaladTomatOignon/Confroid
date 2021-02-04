package fr.uge.confroid.web.service.confroidstorageservice.exceptions;

public class ConfigurationNotFoundException extends RuntimeException {

    public ConfigurationNotFoundException(String name, String version) {
        super("Could not find configuration with name " + name + " and version " + version);
    }
}
