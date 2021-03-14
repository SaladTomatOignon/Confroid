package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationDifferencesTest {

    @Test
    public void integerNoDifferenceTest() {
        Configuration config1 = new Configuration(new Integer(5));
        Configuration config2 = new Configuration(new Integer(5));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void stringNoDifferenceTest() {
        Configuration config1 = new Configuration(new String("Salut"));
        Configuration config2 = new Configuration(new String("Salut"));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void integerDifferenceTest() {
        Configuration config1 = new Configuration(new Integer(5));
        Configuration config2 = new Configuration(new Integer(8));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals( new Configuration(new Integer(8)), diffConfig);
    }

    @Test
    public void stringDifferenceTest() {
        Configuration config1 = new Configuration(new String("Salut"));
        Configuration config2 = new Configuration(new String("Coco"));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new String("Coco")), diffConfig);
    }

    @Test
    public void arrayNoDifferenceTest() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Float(0.23f),
                new Boolean(true),
                new Byte((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Float(0.23f),
                new Boolean(true),
                new Byte((byte) 8)
        }));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void arrayDifferenceTest_add() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new Float(0.23f),
                new Boolean(true),
                new Byte((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new Float(0.23f),
                new Boolean(true),
                new Byte((byte) 8),
                new Integer(5),
                new String("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut")
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_remove() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Byte((byte) 8),
                new Float(0.23f),
                new Boolean(true)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Byte((byte) 8)
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new Float(0.23f),
                new Boolean(true)
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_order() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Float(0.23f),
                new Boolean(true),
                new Byte((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new Byte((byte) 8),
                new Float(0.23f),
                new Integer(5),
                new Boolean(true),
                new String("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new Byte((byte) 8),
                new Float(0.23f),
                new Integer(5),
                new String("Salut")
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_mix() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new Integer(5),
                new String("Salut"),
                new Boolean(false),
                new Boolean(true),
                new Integer(42),
                new Byte((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new Byte((byte) 8),
                new Float(0.23f),
                new Integer(5),
                new Boolean(true),
                new String("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new Byte((byte) 8),
                new Float(0.23f),
                new Integer(5),
                new String("Salut"),
                new Byte((byte) 8)
        })), diffConfig);
    }

    @Test
    public void dictionaryNoDifferenceTest() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                    }}));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void dictionaryDifferenceTest_add() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Vasy", new Byte((byte) 5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Float(42.58f));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Vasy", new Byte((byte) 5));
                        put("Ok", new Float(42.58f));
                    }})), diffConfig);
    }

    @Test
    public void dictionaryDifferenceTest_remove() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Vasy", new Byte((byte) 5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Float(42.58f));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(5));
                        put("Coco", new String("Wesh"));
                        put("Mdr", new Boolean(true));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Vasy", new Byte((byte) 5));
                        put("Ok", new Float(42.58f));
                    }})), diffConfig);
    }

    @Test
    public void dictionaryDifferenceTest_mix() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(789));
                        put("Coco", new Byte((byte) 100));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Float(42.58f));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Coco", new String("Wesh"));
                        put("Salut", new Integer(5));
                        put("Mdr", new Boolean(true));
                        put("Vasy", new Byte((byte) 5));
                        put("Encore un", new String("Normalement c'est ok"));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Vasy", new Byte((byte) 5));
                        put("Coco", new String("Wesh"));
                        put("Ok", new Float(42.58f));
                        put("Salut", new Integer(5));
                        put("Encore un", new String("Normalement c'est ok"));
                    }})), diffConfig);
    }

    @Test
    public void mixDifferenceTest() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(789));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Byte((byte) 100)
                        }));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.5f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est bon"));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Byte((byte) 101)
                        }));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.3f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("Salut", new Integer(5));
                        put("Mdr", new Boolean(true));
                        put("Vasy", new Byte((byte) 5));
                        put("Encore un", new String("Normalement c'est ok"));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Coco", new Array(new Value[] {
                                new Byte((byte) 101)
                        }));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.3f));
                            }}));
                        put("Salut", new Integer(5));
                        put("Vasy", new Byte((byte) 5));
                        put("En plus", new String("C'est bon"));
                        put("Encore un", new String("Normalement c'est ok"));
                    }})), diffConfig);
    }
}
