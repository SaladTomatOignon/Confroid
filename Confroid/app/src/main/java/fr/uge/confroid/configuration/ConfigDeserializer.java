package fr.uge.confroid.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigDeserializer implements JsonDeserializer<Configuration> {

    @Override
    public Configuration deserialize(JsonElement json, Type configType, JsonDeserializationContext context) throws JsonParseException {
        return new Configuration(deserializeValue(json, configType, context));
    }

    private Value deserializeValue(JsonElement json, Type configType, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            // All primitives are nested in jsonObject to save the real type
            if (json.getAsJsonObject().has(Configuration.PRIMITIVE_TYPE_KEYWORD) && json.getAsJsonObject().has(Configuration.PRIMITIVE_KEYWORD)) {
                JsonPrimitive primitive = json.getAsJsonObject().get(Configuration.PRIMITIVE_KEYWORD).getAsJsonPrimitive();

                return deserializePrimitive(primitive,
                        ValueTypes.valueOf(json.getAsJsonObject().get(Configuration.PRIMITIVE_TYPE_KEYWORD).getAsString()),
                        configType, context);
            }

            Map<String, Value> map = json.getAsJsonObject().entrySet().stream().map(entry ->
                new AbstractMap.SimpleEntry<String, Value>(entry.getKey(), deserializeValue(entry.getValue(), configType, context))
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            return new MapValue(map);
        } else if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            Value[] array = new Value[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = deserializeValue(jsonArray.get(i), configType, context);
            }

            return new ArrayValue(array);
        } else if (json.isJsonPrimitive()) {
            return deserializePrimitive(json.getAsJsonPrimitive(), null, configType, context);
        } else {
            return null;
        }
    }

    private Value deserializePrimitive(JsonPrimitive primitive, ValueTypes type, Type configType, JsonDeserializationContext context) throws JsonParseException {
        if (!Objects.isNull(type)) {
            switch (type) {
                case BYTE: return new ByteValue(primitive.getAsByte());
                case INTEGER: return new IntegerValue(primitive.getAsInt());
                case LONG: return new LongValue(primitive.getAsLong());
                case FLOAT: return new FloatValue(primitive.getAsFloat());
                case DOUBLE: return new DoubleValue(primitive.getAsDouble());
                case STRING: return new StringValue(primitive.getAsString());
                case BOOLEAN: return new BooleanValue(primitive.getAsBoolean());
            }
        }

        if (primitive.isBoolean()) {
            return new BooleanValue(primitive.getAsBoolean());
        } else if (primitive.isNumber()) {
            // Since JSON doesn't distinguish between integer and floating fields, we use
            // regex expression to determine which type it is, but it excludes the fact that it can still be a Byte, a Long or a Double
            String num = primitive.getAsString();
            boolean isFloat = num.matches("[-+]?[0-9]*\\.[0-9]+");
            if (isFloat) {
                return new FloatValue(primitive.getAsFloat());
            } else {
                return new IntegerValue(primitive.getAsInt());
            }
        } else if (primitive.isString()) {
            return new StringValue(primitive.getAsString());
        } else {
            return null;
        }
    }
}
