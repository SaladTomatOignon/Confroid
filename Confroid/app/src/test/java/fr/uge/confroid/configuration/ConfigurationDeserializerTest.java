package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationDeserializerTest {

    @Test
    public void jsonToValue_primitive() {
        String jsonInteger = "5";
        String jsonFloat = "8.25";
        String jsonString = "Salut";
        String jsonBoolean = "true";

        Value value;
        value = Configuration.fromJson(jsonInteger).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.isInteger());
        assertEquals(5, (int) value.getInteger());

        value = Configuration.fromJson(jsonFloat).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.isFloat());
        assertEquals(8.25f, value.getFloat(), 0.01);

        value = Configuration.fromJson(jsonString).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.isString());
        assertEquals("Salut", value.getString());

        value = Configuration.fromJson(jsonBoolean).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.isBoolean());
        assertTrue(value.getBoolean());
    }

    @Test
    public void jsonToValue_array() {
        String jsonEmptyArray = "[]";
        String jsonIntegersArray = "[1, 52, 789, -45, 0]";
        int[] integersArray = new int[] {1, 52, 789, -45, 0};

        Value value;
        value = Configuration.fromJson(jsonEmptyArray).getContent();
        assertTrue(value.isArray());
        assertTrue(value.getArray().length == 0);

        value = Configuration.fromJson(jsonIntegersArray).getContent();
        assertTrue(value.isArray());
        Value[] array = value.getArray();
        for (int i = 0; i < array.length; i++) {
            assertTrue(array[i].isPrimitive());
            Value prim = array[i];
            assertEquals((int) prim.getInteger(), integersArray[i]);
        }
    }

    @Test
    public void jsonToValue_dictionary() {
        String json = "{\"key1\":42,\"key2\":\"Coco\",\"key3\":78.23,\"key4\":false}";
        Value value = Configuration.fromJson(json).getContent();

        assertTrue(value.isMap());
        Map<String, Value> map = value.getMap();

        assertTrue(map.get("key1").isInteger());
        assertEquals((int) map.get("key1").getInteger(), 42);

        assertTrue(map.get("key2").isString());
        assertEquals(map.get("key2").getString(), "Coco");

        assertTrue(map.get("key3").isFloat());
        assertEquals((float) map.get("key3").getFloat(), 78.23f, 0.01f);

        assertTrue(map.get("key4").isBoolean());
        assertFalse(map.get("key4").getBoolean());
    }

    @Test
    public void jsonToValue_nestedDictionary() {
        String subJson1 = "{\"key1\":42,\"key2\":\"Coco\",\"key3\":78.23,\"key4\":false}";
        String subJson2 = String.format("{\"key5\":[%s, %s, true, 98]}", subJson1, subJson1);
        String json = String.format("{\"key6\":%s,\"key7\":\"Wesh alors\"}", subJson2);

        Value value = Configuration.fromJson(json).getContent();
        assertTrue(value.isMap());
        assertTrue(value.getMap().get("key6").isMap());
        assertTrue(value.getMap().get("key7").isString());

        Value subJson2Value = value.getMap().get("key6");
        assertTrue(subJson2Value.getMap().get("key5").isArray());
        assertTrue(subJson2Value.getMap().get("key5").getArray()[2].getBoolean());

        Value subJson1Value = subJson2Value.getMap().get("key5").getArray()[0];

        assertTrue(subJson1Value.isMap());
        Map<String, Value> map = subJson1Value.getMap();

        assertTrue(map.get("key1").isInteger());
        assertEquals((int) map.get("key1").getInteger(), 42);

        assertTrue(map.get("key2").isString());
        assertEquals(map.get("key2").getString(), "Coco");

        assertTrue(map.get("key3").isFloat());
        assertEquals((float) map.get("key3").getFloat(), 78.23f, 0.01f);

        assertTrue(map.get("key4").isBoolean());
        assertFalse(map.get("key4").getBoolean());
    }
}
