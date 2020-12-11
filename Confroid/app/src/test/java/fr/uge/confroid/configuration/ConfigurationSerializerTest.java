package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationSerializerTest {

    @Test
    public void valueToJson_primitive() {
        Configuration integer = new Configuration(new Integer(5));
        Configuration floot = new Configuration(new Float(45.12f));
        Configuration bool = new Configuration(new Boolean(true));
        Configuration string = new Configuration(new String("Batman"));

        assertEquals("5", integer.exportToJson());
        assertEquals("45.12", floot.exportToJson());
        assertEquals("true", bool.exportToJson());
        assertEquals("\"Batman\"", string.exportToJson());
    }

    @Test
    public void valueToJson_array() {
        Configuration emptyArray = new Configuration(new Array(new Value[0]));
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floot = new Float(12.8f);
        Boolean bool = new Boolean(false);
        Configuration array = new Configuration(new Array(new Value[]{a, str, floot, bool}));

        assertEquals("[]", emptyArray.exportToJson());
        assertEquals("[42,\"Ali baba\",12.8,false]", array.exportToJson());
    }

    @Test
    public void valueToJson_dictionary() {
        Array emptyArray = new Array(new Value[0]);
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floot = new Float(12.8f);
        Boolean bool = new Boolean(false);
        Array array = new Array(new Value[]{a, str, floot, bool});

        Map<java.lang.String, Value> map = new HashMap<java.lang.String, Value>();
        map.put("int", a);
        map.put("chaine", str);
        map.put("Tableau vide", emptyArray);
        map.put("boolean", bool);
        map.put("Tableau", array);

        Configuration dico = new Configuration(new Dictionary(map));

        assertEquals("{\"Tableau vide\":[],\"boolean\":false,\"chaine\":\"Ali baba\",\"Tableau\":[42,\"Ali baba\",12.8,false],\"int\":42}", dico.exportToJson());
    }

    @Test
    public void valueToJson_nestedDictionary() {
        Array emptyArray = new Array(new Value[0]);
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floot = new Float(12.8f);
        Boolean bool = new Boolean(false);
        Array array = new Array(new Value[]{a, str, floot, bool});

        Map<java.lang.String, Value> map1 = new HashMap<java.lang.String, Value>();
        map1.put("int", a);
        map1.put("chaine", str);
        map1.put("Tableau vide", emptyArray);
        map1.put("boolean", bool);
        map1.put("Tableau", array);

        Map<java.lang.String, Value> map2 = new HashMap<java.lang.String, Value>();
        map2.put("key1", new Boolean(true));
        map2.put("key2", new Float(852.123f));
        map2.put("key3", new Byte((byte) 5));
        map2.put("key4", new Dictionary(map1));

        Configuration dico = new Configuration(new Dictionary(map2));

        assertEquals("{\"key1\":true,\"key2\":852.123,\"key3\":5,\"key4\":{\"Tableau vide\":[],\"boolean\":false,\"chaine\":\"Ali baba\",\"Tableau\":[42,\"Ali baba\",12.8,false],\"int\":42}}",
                dico.exportToJson());
    }
}
