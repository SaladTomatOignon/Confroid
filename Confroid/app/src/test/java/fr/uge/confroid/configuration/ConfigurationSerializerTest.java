package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationSerializerTest {

    @Test
    public void valueToJson_primitive() {
        Configuration integer = new Configuration(new IntegerValue(5));
        Configuration floot = new Configuration(new FloatValue(45.12f));
        Configuration bool = new Configuration(new BooleanValue(true));
        Configuration string = new Configuration(new StringValue("Batman"));

        assertEquals("{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":5}", integer.toJson());
        assertEquals("{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.FLOAT + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":45.12}", floot.toJson());
        assertEquals("{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":true}", bool.toJson());
        assertEquals("{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Batman\"}", string.toJson());
    }

    @Test
    public void valueToJson_array() {
        Configuration emptyArray = new Configuration(new Array(new Value[0]));
        IntegerValue a = new IntegerValue(42);
        StringValue str = new StringValue("Ali baba");
        FloatValue floot = new FloatValue(12.8f);
        BooleanValue bool = new BooleanValue(false);
        Configuration array = new Configuration(new Array(new Value[]{a, str, floot, bool}));

        assertEquals("[]", emptyArray.toJson());
        assertEquals("[{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":42},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Ali baba\"},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.FLOAT + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":12.8},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":false}]", array.toJson());
    }

    @Test
    public void valueToJson_dictionary() {
        Array emptyArray = new Array(new Value[0]);
        IntegerValue a = new IntegerValue(42);
        StringValue str = new StringValue("Ali baba");
        FloatValue floot = new FloatValue(12.8f);
        BooleanValue bool = new BooleanValue(false);
        Array array = new Array(new Value[]{a, str, floot, bool});

        Map<String, Value> map = new HashMap<String, Value>();
        map.put("int", a);
        map.put("chaine", str);
        map.put("Tableau vide", emptyArray);
        map.put("boolean", bool);
        map.put("Tableau", array);

        Configuration dico = new Configuration(new Dictionary(map));

        assertEquals("{\"Tableau vide\":[],\"boolean\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":false},\"chaine\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Ali baba\"},\"Tableau\":[{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":42},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Ali baba\"},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.FLOAT + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":12.8},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":false}],\"int\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":42}}", dico.toJson());
    }

    @Test
    public void valueToJson_nestedDictionary() {
        Array emptyArray = new Array(new Value[0]);
        IntegerValue a = new IntegerValue(42);
        StringValue str = new StringValue("Ali baba");
        FloatValue floot = new FloatValue(12.8f);
        BooleanValue bool = new BooleanValue(false);
        Array array = new Array(new Value[]{a, str, floot, bool});

        Map<String, Value> map1 = new HashMap<String, Value>();
        map1.put("int", a);
        map1.put("chaine", str);
        map1.put("Tableau vide", emptyArray);
        map1.put("boolean", bool);
        map1.put("Tableau", array);

        Map<String, Value> map2 = new HashMap<String, Value>();
        map2.put("key1", new BooleanValue(true));
        map2.put("key2", new FloatValue(852.123f));
        map2.put("key3", new ByteValue((byte) 5));
        map2.put("key4", new Dictionary(map1));

        Configuration dico = new Configuration(new Dictionary(map2));

        assertEquals("{\"key1\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":true},\"key2\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.FLOAT + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":852.123},\"key3\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BYTE + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":5},\"key4\":{\"Tableau vide\":[],\"boolean\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":false},\"chaine\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Ali baba\"},\"Tableau\":[{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":42},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.STRING + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":\"Ali baba\"},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.FLOAT + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":12.8},{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.BOOLEAN + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":false}],\"int\":{\"" + Configuration.PRIMITIVE_TYPE_KEYWORD + "\":\"" + ValueTypes.INTEGER + "\",\"" + Configuration.PRIMITIVE_KEYWORD + "\":42}}}",
                dico.toJson());
    }
}
