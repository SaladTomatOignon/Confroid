package fr.uge.confroid.configuration;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ConfigurationDifferencesTest {

    @Test
    public void integerNoDifferenceTest() {
        Configuration config1 = new Configuration(new IntegerValue(5));
        Configuration config2 = new Configuration(new IntegerValue(5));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void stringNoDifferenceTest() {
        Configuration config1 = new Configuration(new StringValue("Salut"));
        Configuration config2 = new Configuration(new StringValue("Salut"));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void integerDifferenceTest() {
        Configuration config1 = new Configuration(new IntegerValue(5));
        Configuration config2 = new Configuration(new IntegerValue(8));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals( new Configuration(new IntegerValue(8)), diffConfig);
    }

    @Test
    public void stringDifferenceTest() {
        Configuration config1 = new Configuration(new StringValue("Salut"));
        Configuration config2 = new Configuration(new StringValue("Coco"));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new StringValue("Coco")), diffConfig);
    }

    @Test
    public void arrayNoDifferenceTest() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new FloatValue(0.23f),
                new BooleanValue(true),
                new ByteValue((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new FloatValue(0.23f),
                new BooleanValue(true),
                new ByteValue((byte) 8)
        }));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void arrayDifferenceTest_add() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new FloatValue(0.23f),
                new BooleanValue(true),
                new ByteValue((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new FloatValue(0.23f),
                new BooleanValue(true),
                new ByteValue((byte) 8),
                new IntegerValue(5),
                new StringValue("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut")
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_remove() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new ByteValue((byte) 8),
                new FloatValue(0.23f),
                new BooleanValue(true)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new ByteValue((byte) 8)
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new FloatValue(0.23f),
                new BooleanValue(true)
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_order() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new FloatValue(0.23f),
                new BooleanValue(true),
                new ByteValue((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new ByteValue((byte) 8),
                new FloatValue(0.23f),
                new IntegerValue(5),
                new BooleanValue(true),
                new StringValue("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new ByteValue((byte) 8),
                new FloatValue(0.23f),
                new IntegerValue(5),
                new StringValue("Salut")
        })), diffConfig);
    }

    @Test
    public void arrayDifferenceTest_mix() {
        Configuration config1 = new Configuration(new Array(new Value[] {
                new IntegerValue(5),
                new StringValue("Salut"),
                new BooleanValue(false),
                new BooleanValue(true),
                new IntegerValue(42),
                new ByteValue((byte) 8)
        }));

        Configuration config2 = new Configuration(new Array(new Value[] {
                new ByteValue((byte) 8),
                new FloatValue(0.23f),
                new IntegerValue(5),
                new BooleanValue(true),
                new StringValue("Salut")
        }));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(new Array(new Value[] {
                new ByteValue((byte) 8),
                new FloatValue(0.23f),
                new IntegerValue(5),
                new StringValue("Salut"),
                new ByteValue((byte) 8)
        })), diffConfig);
    }

    @Test
    public void dictionaryNoDifferenceTest() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                    }}));

        assertNull(Configuration.difference(config1, config2).getContent());
    }

    @Test
    public void dictionaryDifferenceTest_add() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Vasy", new ByteValue((byte) 5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new FloatValue(42.58f));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Vasy", new ByteValue((byte) 5));
                        put("Ok", new FloatValue(42.58f));
                    }})), diffConfig);
    }

    @Test
    public void dictionaryDifferenceTest_remove() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Vasy", new ByteValue((byte) 5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new FloatValue(42.58f));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(5));
                        put("Coco", new StringValue("Wesh"));
                        put("Mdr", new BooleanValue(true));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Vasy", new ByteValue((byte) 5));
                        put("Ok", new FloatValue(42.58f));
                    }})), diffConfig);
    }

    @Test
    public void dictionaryDifferenceTest_mix() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(789));
                        put("Coco", new ByteValue((byte) 100));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new FloatValue(42.58f));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Coco", new StringValue("Wesh"));
                        put("Salut", new IntegerValue(5));
                        put("Mdr", new BooleanValue(true));
                        put("Vasy", new ByteValue((byte) 5));
                        put("Encore un", new StringValue("Normalement c'est ok"));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Vasy", new ByteValue((byte) 5));
                        put("Coco", new StringValue("Wesh"));
                        put("Ok", new FloatValue(42.58f));
                        put("Salut", new IntegerValue(5));
                        put("Encore un", new StringValue("Normalement c'est ok"));
                    }})), diffConfig);
    }

    @Test
    public void mixDifferenceTest() {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(789));
                        put("Coco", new Array(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new ByteValue((byte) 100)
                        }));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new Dictionary(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.5f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est bon"));
                    }}));

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Coco", new Array(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new ByteValue((byte) 101)
                        }));
                        put("Ok", new Dictionary(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.3f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("Salut", new IntegerValue(5));
                        put("Mdr", new BooleanValue(true));
                        put("Vasy", new ByteValue((byte) 5));
                        put("Encore un", new StringValue("Normalement c'est ok"));
                    }}));

        Configuration diffConfig = Configuration.difference(config1, config2);
        assertEquals(new Configuration(
                new Dictionary(new HashMap<String, Value>() {
                    {
                        put("Coco", new Array(new Value[] {
                                new ByteValue((byte) 101)
                        }));
                        put("Ok", new Dictionary(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.3f));
                            }}));
                        put("Salut", new IntegerValue(5));
                        put("Vasy", new ByteValue((byte) 5));
                        put("En plus", new StringValue("C'est bon"));
                        put("Encore un", new StringValue("Normalement c'est ok"));
                    }})), diffConfig);
    }
}
