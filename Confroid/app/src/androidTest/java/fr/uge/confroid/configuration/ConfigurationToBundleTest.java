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
        Array emptyArray = new Array(new Value[0]);
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floot = new Float(12.8f);
        Boolean bool = new Boolean(false);
        Array array = new Array(new Value[]{a, str, floot, bool});

        Map<java.lang.String, Value> map1 = new HashMap<>();
        map1.put("int", a);
        map1.put("chaine", str);
        map1.put("Tableau vide", emptyArray);
        map1.put("boolean", bool);
        map1.put("Tableau", array);

        Map<java.lang.String, Value> map2 = new HashMap<>();
        map2.put("key1", new Boolean(true));
        map2.put("key2", new Float(852.123f));
        map2.put("key3", new Byte((byte) 5));
        map2.put("key4", new Dictionary(map1));

        Configuration dico = new Configuration(new Dictionary(map2));
        Bundle bundle = dico.toBundle();

        assertEquals(4, bundle.size());
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
    }
}