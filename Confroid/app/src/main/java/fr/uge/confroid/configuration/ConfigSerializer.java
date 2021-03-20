package fr.uge.confroid.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        if (config.isMap()) {
            Map<String, JsonElement> map = config.getMap().entrySet().stream().map(entry ->
                    new AbstractMap.SimpleEntry<>(entry.getKey(), serializeValue(entry.getValue(), configType, context))
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            Gson gson = new Gson();
            return gson.toJsonTree(map).getAsJsonObject();
        } else if (config.isArray()) {
            JsonElement[] array = Arrays.stream(config.getArray()).map(value -> serializeValue(value, configType, context)).toArray(JsonElement[]::new);

            Gson gson = new Gson();
            return gson.toJsonTree(array).getAsJsonArray();
        } else if (config.isPrimitive()) {
            JsonPrimitive primitive;
            JsonObject wrapper = new JsonObject();
            wrapper.add(Configuration.PRIMITIVE_TYPE_KEYWORD, new JsonPrimitive(config.valueType().name()));

            switch (config.valueType()) {
                case BYTE: primitive = new JsonPrimitive(config.getByte()); break;
                case INTEGER: primitive = new JsonPrimitive(config.getInteger()); break;
                case LONG: primitive = new JsonPrimitive(config.getLong()); break;
                case FLOAT: primitive = new JsonPrimitive(config.getFloat()); break;
                case DOUBLE: primitive = new JsonPrimitive(config.getDouble()); break;
                case STRING: primitive = new JsonPrimitive(config.getString()); break;
                case BOOLEAN: primitive = new JsonPrimitive(config.getBoolean()); break;
                default: return null;
            }

            wrapper.add(Configuration.PRIMITIVE_KEYWORD, primitive);
            return wrapper;
        } else {
            return null;
        }
    }
}
