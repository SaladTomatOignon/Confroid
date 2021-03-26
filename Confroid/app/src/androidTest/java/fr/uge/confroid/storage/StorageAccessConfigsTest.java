package fr.uge.confroid.storage;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Environment;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.uge.confroid.configuration.ArrayValue;
import fr.uge.confroid.configuration.BooleanValue;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.MapValue;
import fr.uge.confroid.configuration.FloatValue;
import fr.uge.confroid.configuration.IntegerValue;
import fr.uge.confroid.configuration.StringValue;
import fr.uge.confroid.configuration.Value;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StorageAccessConfigsTest {

    private static final Context appContext = getApplicationContext();
    private static File testFile;

    @BeforeClass
    public static void initTestFile() {
        ContextWrapper cw = new ContextWrapper(appContext);
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        testFile = new File(directory, "confroidTestFile" + ".json");

    }

    @Before
    public void deleteTestFile() {
        boolean removed = testFile.delete();
    }

    @Test
    public void writeAndReadOneConfigTest() throws IOException {
        Configuration config = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(789));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new IntegerValue(100)
                        }));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.5f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est bon"));
                    }})
        );

        ConfroidPackage pkg = new ConfroidPackage("fr.uge.test", 1, new Date(), config);
        ConfroidStorage.writeConfig(pkg, Uri.fromFile(testFile), appContext);

        Map<String, Map<Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

        assertEquals(1, collection.size());
        assertEquals(1, collection.get("fr.uge.test").size());
        assertEquals(pkg, collection.get("fr.uge.test").get(1));
    }

    @Test
    public void writeAndReadThreeConfigsTest() throws IOException {
        Configuration config1 = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(789));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new IntegerValue(100)
                        }));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.5f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est bon"));
                    }})
        );

        ConfroidPackage pkg1 = new ConfroidPackage("fr.uge.test", 1, new Date(), config1);

        Configuration config2 = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(790));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                        }));
                        put("Mdr", new BooleanValue(false));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.8f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est ok"));
                    }})
        );

        ConfroidPackage pkg2 = new ConfroidPackage("fr.uge.test", 2, new Date(), config2, "Release");

        Configuration config3 = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new StringValue("Ba ouais"));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new IntegerValue(101)
                        }));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Finally", new BooleanValue(true));
                            }}));
                        put("En plus", new StringValue("C'est bon"));
                    }})
        );

        ConfroidPackage pkg3 = new ConfroidPackage("fr.uge.test2", 1, new Date(), config3);

        ConfroidStorage.writeConfig(pkg1, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg2, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg3, Uri.fromFile(testFile), appContext);

        Map<String, Map<Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

        assertEquals(2, collection.size());

        assertEquals(2, collection.get("fr.uge.test").size());
        assertEquals(pkg1, collection.get("fr.uge.test").get(1));
        assertEquals(pkg2, collection.get("fr.uge.test").get(2));

        assertEquals(1, collection.get("fr.uge.test2").size());
        assertEquals(pkg3, collection.get("fr.uge.test2").get(1));
    }

    @Test
    public void updateConfigTest() throws IOException {
        Configuration config1 = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(789));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42),
                                new IntegerValue(100)
                        }));
                        put("Mdr", new BooleanValue(true));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.5f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est bon"));
                    }})
        );

        ConfroidPackage pkg1 = new ConfroidPackage("fr.uge.test", 1, new Date(), config1);

        Configuration config2 = new Configuration(
                new MapValue(new HashMap<String, Value>() {
                    {
                        put("Salut", new IntegerValue(790));
                        put("Coco", new ArrayValue(new Value[] {
                                new IntegerValue(5),
                                new IntegerValue(42)
                        }));
                        put("Mdr", new BooleanValue(false));
                        put("Ok", new MapValue(new HashMap<String, Value>() {
                            {
                                put("Mdr", new FloatValue(42.8f));
                                put("Finally", new BooleanValue(false));
                            }}));
                        put("En plus", new StringValue("C'est ok"));
                    }})
        );

        ConfroidPackage pkg2 = new ConfroidPackage("fr.uge.test", 1, new Date(), config2, "Tag");

        ConfroidStorage.writeConfig(pkg1, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg2, Uri.fromFile(testFile), appContext);

        Map<String, Map<Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

        assertEquals(1, collection.size());

        assertEquals(1, collection.get("fr.uge.test").size());
        assertNotEquals(pkg1, collection.get("fr.uge.test").get(1));
        assertEquals(pkg2, collection.get("fr.uge.test").get(1));
    }
}
