package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationDeserializerTest {

    @Test
    public void jsonToValue_primitive() {
        java.lang.String jsonInteger = "5";
        java.lang.String jsonFloat = "8.25";
        java.lang.String jsonString = "Salut";
        java.lang.String jsonBoolean = "true";

        Value value;
        value = Configuration.importFromJson(jsonInteger).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.getPrimitive().isInteger());
        assertEquals(5, (int) value.getPrimitive().getInteger());

        value = Configuration.importFromJson(jsonFloat).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.getPrimitive().isFloat());
        assertEquals(8.25f, value.getPrimitive().getFloat(), 0.01);

        value = Configuration.importFromJson(jsonString).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.getPrimitive().isString());
        assertEquals("Salut", value.getPrimitive().getString());

        value = Configuration.importFromJson(jsonBoolean).getContent();
        assertTrue(value.isPrimitive());
        assertTrue(value.getPrimitive().isBoolean());
        assertTrue(value.getPrimitive().getBoolean());
    }

    @Test
    public void jsonToValue_array() {
        java.lang.String jsonEmptyArray = "[]";
        java.lang.String jsonIntegersArray = "[1, 52, 789, -45, 0]";
        int[] integersArray = new int[] {1, 52, 789, -45, 0};

        Value value;
        value = Configuration.importFromJson(jsonEmptyArray).getContent();
        assertTrue(value.isArray());
        assertTrue(value.getArray().length == 0);

        value = Configuration.importFromJson(jsonIntegersArray).getContent();
        assertTrue(value.isArray());
        Value[] array = value.getArray();
        for (int i = 0; i < array.length; i++) {
            assertTrue(array[i].isPrimitive());
            Primitive prim = array[i].getPrimitive();
            assertEquals((int) prim.getInteger(), integersArray[i]);
        }
    }

    @Test
    public void jsonToValue_dictionary() {
        java.lang.String json = "{\"key1\":42,\"key2\":\"Coco\",\"key3\":78.23,\"key4\":false}";
        Value value = Configuration.importFromJson(json).getContent();

        assertTrue(value.isDictionary());
        Map<java.lang.String, Value> map = value.getMap();

        assertTrue(map.get("key1").getPrimitive().isInteger());
        assertEquals((int) map.get("key1").getPrimitive().getInteger(), 42);

        assertTrue(map.get("key2").getPrimitive().isString());
        assertEquals(map.get("key2").getPrimitive().getString(), "Coco");

        assertTrue(map.get("key3").getPrimitive().isFloat());
        assertEquals((float) map.get("key3").getPrimitive().getFloat(), 78.23f, 0.01f);

        assertTrue(map.get("key4").getPrimitive().isBoolean());
        assertFalse(map.get("key4").getPrimitive().getBoolean());
    }

    @Test
    public void jsonToValue_nestedDictionary() {
        java.lang.String subJson1 = "{\"key1\":42,\"key2\":\"Coco\",\"key3\":78.23,\"key4\":false}";
        java.lang.String subJson2 = java.lang.String.format("{\"key5\":[%s, %s, true, 98]}", subJson1, subJson1);
        java.lang.String json = java.lang.String.format("{\"key6\":%s,\"key7\":\"Wesh alors\"}", subJson2);

        Value value = Configuration.importFromJson(json).getContent();
        assertTrue(value.isDictionary());
        assertTrue(value.getMap().get("key6").isDictionary());
        assertTrue(value.getMap().get("key7").getPrimitive().isString());

        Value subJson2Value = value.getMap().get("key6");
        assertTrue(subJson2Value.getMap().get("key5").isArray());
        assertTrue(subJson2Value.getMap().get("key5").getArray()[2].getPrimitive().getBoolean());

        Value subJson1Value = subJson2Value.getMap().get("key5").getArray()[0];

        assertTrue(subJson1Value.isDictionary());
        Map<java.lang.String, Value> map = subJson1Value.getMap();

        assertTrue(map.get("key1").getPrimitive().isInteger());
        assertEquals((int) map.get("key1").getPrimitive().getInteger(), 42);

        assertTrue(map.get("key2").getPrimitive().isString());
        assertEquals(map.get("key2").getPrimitive().getString(), "Coco");

        assertTrue(map.get("key3").getPrimitive().isFloat());
        assertEquals((float) map.get("key3").getPrimitive().getFloat(), 78.23f, 0.01f);

        assertTrue(map.get("key4").getPrimitive().isBoolean());
        assertFalse(map.get("key4").getPrimitive().getBoolean());
    }
}
