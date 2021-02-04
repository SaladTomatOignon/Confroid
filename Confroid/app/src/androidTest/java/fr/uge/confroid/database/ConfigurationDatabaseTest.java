package fr.uge.confroid.database;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

import fr.uge.confroid.configuration.Array;
import fr.uge.confroid.configuration.Boolean;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Dictionary;
import fr.uge.confroid.configuration.Float;
import fr.uge.confroid.configuration.Integer;
import fr.uge.confroid.configuration.String;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.storage.Database;

@RunWith(AndroidJUnit4.class)
public class ConfigurationDatabaseTest {

    //ok
    @Test
    public void saveConfig() {

        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("db_test");
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_test", null, 1);
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floo = new Float(12.8f);
        Boolean boo = new Boolean(false);
        Array arra = new Array(new Value[]{a, str, floo, boo});
        Array emptyArra = new Array(new Value[0]);
        // Test all type of Configuration
        Configuration integer = new Configuration(new Integer(5));
        Configuration floot = new Configuration(new Float(45.12f));
        Configuration bool = new Configuration(new Boolean(true));
        Configuration string = new Configuration(new String("Batman"));
        Configuration emptyArray = new Configuration(new Array(new Value[0]));
        Configuration array = new Configuration(new Array(new Value[]{a, str, floo, boo}));

        Map<java.lang.String, Value> map = new HashMap<java.lang.String, Value>();
        map.put("int", a);
        map.put("chaine", str);
        map.put("Tableau vide",emptyArra);
        map.put("boolean", boo);
        map.put("Tableau", arra);
        Configuration dico = new Configuration(new Dictionary(map));

        ConfroidPackage confPackageInteger = new ConfroidPackage("configInt", "1", integer, "");
        ConfroidPackage confPackageInteger2 = new ConfroidPackage("configInt", "2", integer, "");
        ConfroidPackage confPackageInteger3 = new ConfroidPackage("configInt", "3", integer, "");
        ConfroidPackage confPackageFloot = new ConfroidPackage("confiFloot", "1", floot, "");
        ConfroidPackage confPackageBool = new ConfroidPackage("configBool", "1", bool, "");
        ConfroidPackage confPackageString = new ConfroidPackage("configString", "1", string, "");
        ConfroidPackage confPackageEmptyArray= new ConfroidPackage("configEmptyArray", "1", emptyArray, "");
        ConfroidPackage confPackageArray= new ConfroidPackage("configArray", "1", array, "");
        ConfroidPackage confPackageDico= new ConfroidPackage("configDico", "1", dico, "");
        assertEquals(true, database.saveConfig(confPackageInteger));
        assertEquals(true, database.saveConfig(confPackageInteger2));
        assertEquals(true, database.saveConfig(confPackageInteger3));
        assertEquals(true, database.saveConfig(confPackageFloot));
        assertEquals(true, database.saveConfig(confPackageBool));
        assertEquals(true, database.saveConfig(confPackageString));
        assertEquals(true, database.saveConfig(confPackageEmptyArray));
        assertEquals(true, database.saveConfig(confPackageArray));
        assertEquals(true, database.saveConfig(confPackageDico));

        assertEquals(false, database.saveConfig(confPackageInteger));
        assertEquals(false, database.saveConfig(confPackageFloot));
        assertEquals(false, database.saveConfig(confPackageBool));
    }

    @Test
    public void getConfig() {
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_test", null, 1);
        Optional<ConfroidPackage>  configInt = database.getConfig("configInt","1");
        Integer a = new Integer(42);
        String str = new String("Ali baba");
        Float floo = new Float(12.8f);
        Boolean boo = new Boolean(false);
        Array arra = new Array(new Value[]{a, str, floo, boo});
        Array emptyArra = new Array(new Value[0]);
        if (configInt.isPresent()) {
            assertEquals("configInt", configInt.get().getName());
            assertEquals("1", configInt.get().getVersion());
            assertEquals("", configInt.get().getTag());
            Configuration integer = new Configuration(new Integer(5));
            assertEquals(integer, configInt.get().getConfig());

        }
        Optional<ConfroidPackage>  confiFloot = database.getConfig("confiFloot","1");
        if (confiFloot.isPresent()) {
            assertEquals("confiFloot", confiFloot.get().getName());
            assertEquals("1", confiFloot.get().getVersion());
            assertEquals("", confiFloot.get().getTag());
            // Todo equals config
            Configuration floot = new Configuration(new Float(45.12f));
            assertEquals(floot, confiFloot.get().getConfig());
        }
        Optional<ConfroidPackage>  configArray = database.getConfig("configArray","1");
        if (configArray.isPresent()) {

            assertEquals("configArray", configArray.get().getName());
            assertEquals("1", configArray.get().getVersion());
            assertEquals("", configArray.get().getTag());
            // Todo equals config
            Configuration array = new Configuration(new Array(new Value[]{a, str, floo, boo}));
            assertEquals(array, configArray.get().getConfig());
        }
        Map<java.lang.String, Value> map = new HashMap<java.lang.String, Value>();
        map.put("int", a);
        map.put("chaine", str);
        map.put("Tableau vide",emptyArra);
        map.put("boolean", boo);
        map.put("Tableau", arra);
        Configuration dico = new Configuration(new Dictionary(map));
        Optional<ConfroidPackage>  configDico = database.getConfig("configDico","1");
        if (configArray.isPresent()) {
            assertEquals("configDico", configDico.get().getName());
            assertEquals("1", configDico.get().getVersion());
            assertEquals("", configDico.get().getTag());
            Configuration array = new Configuration(new Array(new Value[]{a, str, floo, boo}));
            assertEquals(array, configArray.get().getConfig());
            assertEquals(dico, configDico.get().getConfig());
        }
    }

    //OK
    @Test
    public void getConfigsName(){
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_test", null, 1);
        List<java.lang.String> ConfigsName = List.of("confiFloot", "configArray", "configBool", "configDico", "configEmptyArray", "configInt", "configString");
        assertEquals(ConfigsName, database.getConfigsName());
    }

    //OK
    @Test
    public void getConfigsNameEmpty(){
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_testEmpty", null, 1);
        assertEquals(List.of(), database.getConfigsName());
    }

    // Todo  chaque tester
    @Test
    public void getAllConfigs() {
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_test", null, 1);
        assertEquals(3, database.getAllConfigs("configInt").size());
        assertEquals(1, database.getAllConfigs("confiFloot").size());
    }

    @Test
    public void getAllConfigseEmpty() {
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_testEmpty", null, 1);
        assertEquals(List.of(),  database.getAllConfigs("configInt"));
    }

    @Test
    public void updateConfig() {
        Database database = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext(), "db_testUpdate", null, 1);
        Configuration integer = new Configuration(new Integer(2));
        ConfroidPackage confPackageInteger = new ConfroidPackage("configInt", "1", integer, "");
       database.saveConfig(confPackageInteger);
        Configuration newInteger = new Configuration(new Integer(9));

        database.updateConfig("configInt", "1", newInteger);
    }
}

