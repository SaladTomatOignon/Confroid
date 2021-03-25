package fr.uge.confroid.configuration;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ConfigurationToBundleTest {

    @Test
    public void configToBundle_nestedDictionary() {
        ArrayValue emptyArray = new ArrayValue(new Value[0]);
        IntegerValue a = new IntegerValue(42);
        StringValue str = new StringValue("Ali baba");
        FloatValue floot = new FloatValue(12.8f);
        BooleanValue bool = new BooleanValue(false);
        ArrayValue array = new ArrayValue(new Value[]{a, str, floot, bool});

        Map<String, Value> map1 = new HashMap<>();
        map1.put("int", a);
        map1.put("chaine", str);
        map1.put("Tableau vide", emptyArray);
        map1.put("boolean", bool);
        map1.put("Tableau", array);

        Map<String, Value> map2 = new HashMap<>();
        map2.put("key1", new BooleanValue(true));
        map2.put("key2", new FloatValue(852.123f));
        map2.put("key3", new ByteValue((byte) 5));
        map2.put("key4", new MapValue(map1));
        map2.put("key5", new LongValue(123456789L));
        map2.put("key6", new DoubleValue(123.456789));

        Configuration dico = new Configuration(new MapValue(map2));
        Bundle bundle = dico.toBundle();

        assertEquals(6, bundle.size());
        assertTrue(bundle.getBoolean("key1"));
        assertEquals(852.123f, bundle.getFloat("key2"), 0.001f);
        assertEquals(5, bundle.getByte("key3"));

        Bundle subBundle = bundle.getBundle("key4");
        assertTrue(subBundle.getBundle("Tableau vide").isEmpty());

        Bundle subBundleTableau = subBundle.getBundle("Tableau");
        assertEquals(4, subBundleTableau.size());
        assertEquals(42, subBundleTableau.getInt("0"));
        assertEquals("Ali baba", subBundleTableau.getString("1"));
        assertEquals(12.8f, subBundleTableau.getFloat("2"), 0.1f);
        assertFalse(subBundleTableau.getBoolean("3"));

        assertEquals(123456789L, bundle.getLong("key5"));
        assertEquals(123.456789, bundle.getDouble("key6"), 0.0001);
    }
}
