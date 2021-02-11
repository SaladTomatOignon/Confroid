package fr.uge.confroid.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigSerializer implements JsonSerializer<Configuration> {

    @Override
    public JsonElement serialize(Configuration config, Type configType, JsonSerializationContext context) {
        return serializeValue(config.getContent(), configType, context);
    }

    private JsonElement serializeValue(Value config, Type configType, JsonSerializationContext context) {
        if (config.isDictionary()) {
            Map<java.lang.String, JsonElement> map = config.getMap().entrySet().stream().map(entry ->
                    new AbstractMap.SimpleEntry<java.lang.String, JsonElement>(entry.getKey(), serializeValue(entry.getValue(), configType, context))
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            Gson gson = new Gson();
            return gson.toJsonTree(map).getAsJsonObject();
        } else if (config.isArray()) {
            JsonElement[] array = Arrays.stream(config.getArray()).map(value -> serializeValue(value, configType, context)).toArray(JsonElement[]::new);

            Gson gson = new Gson();
            return gson.toJsonTree(array).getAsJsonArray();
        } else if (config.isPrimitive()) {
            if (config.getPrimitive().isBoolean()) {
                return new JsonPrimitive(config.getPrimitive().getBoolean());
            } else if (config.getPrimitive().isByte()) {
                return new JsonPrimitive(config.getPrimitive().getByte());
            } else if (config.getPrimitive().isInteger()) {
                return new JsonPrimitive(config.getPrimitive().getInteger());
            } else if (config.getPrimitive().isFloat()) {
                return new JsonPrimitive(config.getPrimitive().getFloat());
            } else if (config.getPrimitive().isString()) {
                java.lang.String str = config.getPrimitive().getString();
                return new JsonPrimitive(config.getPrimitive().getString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
