package fr.uge.confroid.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.uge.confroid.storage.serialization.ConfigDeserializer;
import fr.uge.confroid.storage.serialization.ConfigSerializer;

public class Configuration {
    private final Value content;

    public Configuration(Value content) {
        this.content = content;
    }

    public Value getContent() {
        return content;
    }

    public static Configuration importFromJson(java.lang.String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigDeserializer()).create();
        return gson.fromJson(json, Configuration.class);
    }

    public java.lang.String exportToJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Configuration.class, new ConfigSerializer()).create();
        return gson.toJson(this);
    }
}
