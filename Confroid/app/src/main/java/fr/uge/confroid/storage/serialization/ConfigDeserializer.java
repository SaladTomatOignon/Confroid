package fr.uge.confroid.storage.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.uge.confroid.configuration.Array;
import fr.uge.confroid.configuration.Boolean;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Dictionary;
import fr.uge.confroid.configuration.Float;
import fr.uge.confroid.configuration.Integer;
import fr.uge.confroid.configuration.String;
import fr.uge.confroid.configuration.Value;

public class ConfigDeserializer implements JsonDeserializer<Configuration> {

    @Override
    public Configuration deserialize(JsonElement json, Type configType, JsonDeserializationContext context) throws JsonParseException {
        return new Configuration(deserializeValue(json, configType, context));
    }

    private Value deserializeValue(JsonElement json, Type configType, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            Map<java.lang.String, Value> map = json.getAsJsonObject().entrySet().stream().map(entry ->
                new AbstractMap.SimpleEntry<java.lang.String, Value>(entry.getKey(), deserializeValue(entry.getValue(), configType, context))
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            return new Dictionary(map);
        } else if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            Value[] array = new Value[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = deserializeValue(jsonArray.get(i), configType, context);
            }

            return new Array(array);
        } else if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isBoolean()) {
                return new Boolean(primitive.getAsBoolean());
            } else if (primitive.isNumber()) {
                // Since JSON doesn't distinguish between integer and floating fields, we use
                // regex expression to determine which type it is, but it excludes the fact that it can still be a Byte
                java.lang.String num = primitive.getAsString();
                boolean isFloat = num.matches("[-+]?[0-9]*\\.[0-9]+");
                if (isFloat) {
                    return new Float(primitive.getAsFloat());
                } else {
                    return new Integer(primitive.getAsInt());
                }
            } else if (primitive.isString()) {
                return new String(primitive.getAsString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
