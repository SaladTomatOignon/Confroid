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

import fr.uge.confroid.configuration.Array;
import fr.uge.confroid.configuration.Boolean;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Dictionary;
import fr.uge.confroid.configuration.Float;
import fr.uge.confroid.configuration.Integer;
import fr.uge.confroid.configuration.String;
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
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(789));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Integer(100)
                        }));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.5f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est bon"));
                    }})
        );

        ConfroidPackage pkg = new ConfroidPackage("fr.uge.test", 1, new Date(), config);
        ConfroidStorage.writeConfig(pkg, Uri.fromFile(testFile), appContext);

        Map<java.lang.String, Map<java.lang.Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

        assertEquals(1, collection.size());
        assertEquals(1, collection.get("fr.uge.test").size());
        assertEquals(pkg, collection.get("fr.uge.test").get(1));
    }

    @Test
    public void writeAndReadThreeConfigsTest() throws IOException {
        Configuration config1 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(789));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Integer(100)
                        }));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.5f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est bon"));
                    }})
        );

        ConfroidPackage pkg1 = new ConfroidPackage("fr.uge.test", 1, new Date(), config1);

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(790));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                        }));
                        put("Mdr", new Boolean(false));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.8f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est ok"));
                    }})
        );

        ConfroidPackage pkg2 = new ConfroidPackage("fr.uge.test", 2, new Date(), config2, "Release");

        Configuration config3 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new String("Ba ouais"));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Integer(101)
                        }));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Finally", new Boolean(true));
                            }}));
                        put("En plus", new String("C'est bon"));
                    }})
        );

        ConfroidPackage pkg3 = new ConfroidPackage("fr.uge.test2", 1, new Date(), config3);

        ConfroidStorage.writeConfig(pkg1, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg2, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg3, Uri.fromFile(testFile), appContext);

        Map<java.lang.String, Map<java.lang.Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

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
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(789));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42),
                                new Integer(100)
                        }));
                        put("Mdr", new Boolean(true));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.5f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est bon"));
                    }})
        );

        ConfroidPackage pkg1 = new ConfroidPackage("fr.uge.test", 1, new Date(), config1);

        Configuration config2 = new Configuration(
                new Dictionary(new HashMap<java.lang.String, Value>() {
                    {
                        put("Salut", new Integer(790));
                        put("Coco", new Array(new Value[] {
                                new Integer(5),
                                new Integer(42)
                        }));
                        put("Mdr", new Boolean(false));
                        put("Ok", new Dictionary(new HashMap<java.lang.String, Value>() {
                            {
                                put("Mdr", new Float(42.8f));
                                put("Finally", new Boolean(false));
                            }}));
                        put("En plus", new String("C'est ok"));
                    }})
        );

        ConfroidPackage pkg2 = new ConfroidPackage("fr.uge.test", 1, new Date(), config2, "Tag");

        ConfroidStorage.writeConfig(pkg1, Uri.fromFile(testFile), appContext);
        ConfroidStorage.writeConfig(pkg2, Uri.fromFile(testFile), appContext);

        Map<java.lang.String, Map<java.lang.Integer, ConfroidPackage>> collection = ConfroidStorage.readConfigs(Uri.fromFile(testFile), appContext);

        assertEquals(1, collection.size());

        assertEquals(1, collection.get("fr.uge.test").size());
        assertNotEquals(pkg1, collection.get("fr.uge.test").get(1));
        assertEquals(pkg2, collection.get("fr.uge.test").get(1));
    }
}
