package fr.uge.confroid.configuration;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.uge.confroidlib.BundleUtils;
import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.Description;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.validators.CreditCardChecker;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BundleToConfigurationTest {

    @Test
    public void bundleToConfig_nestedDictionary() {
        Bundle bundlePrimitive = new Bundle();
        bundlePrimitive.putString(BundleUtils.PRIMITIVE_KEYWORD, "Hello");

        Bundle bundle1 = new Bundle();
        bundle1.putInt("0", 789);
        bundle1.putFloat("1", 12.45f);
        bundle1.putString("2", "Ok Many");
        bundle1.putLong("3", 123456789L);
        bundle1.putDouble("4", 123.456);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("entier", 42);
        bundle2.putBoolean("bouboul", true);
        bundle2.putBundle("nested", bundle1);
        bundle2.putBundle("UnDernier", bundlePrimitive);
        Value config = Configuration.fromBundle(bundle2).getContent();

        assertTrue(config.isMap());
        assertEquals(42, (int) config.getMap().get("entier").getInteger());
        assertTrue(config.getMap().get("nested").isArray());
        assertEquals("Ok Many", config.getMap().get("nested").getArray()[2].getString());
        assertEquals((long) 123456789L, (long) config.getMap().get("nested").getArray()[3].getLong());
        assertEquals(123.456, config.getMap().get("nested").getArray()[4].getDouble(), 0.001);
        assertTrue(config.getMap().get("UnDernier").isPrimitive());
        assertEquals("Hello", config.getMap().get("UnDernier").getString());
    }

    @Test
    public void arrayToConfigTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleUtils.ID_KEYWORD, 1);
        bundle.putString(BundleUtils.CLASS_KEYWORD, int[].class.getName());
        bundle.putInt("0", 42);
        bundle.putInt("1", 789);
        bundle.putInt("2", 123);
        bundle.putInt("3", 0);

        Value config = Configuration.fromBundle(bundle).getContent();

        assertTrue(config.isArray());
        assertEquals(6, config.getArray().length);
    }

    @Test
    public void MixObjectsClassTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        MixObjectsClass mixObjectsClass = new MixObjectsClass();
        mixObjectsClass.initValues();

        Bundle bundle = BundleUtils.convertToBundleReflection(mixObjectsClass);
        Configuration config = Configuration.fromBundle(bundle);

        Bundle bundleConverted = config.toBundle();
        MixObjectsClass mixObjectsClassConverted = (MixObjectsClass) BundleUtils.convertFromBundle(bundleConverted);

        assertEquals(mixObjectsClass, mixObjectsClassConverted);
    }

    @Test
    public void getReferencedIdTest() {
        MixObjectsClass mixObjectsClass = new MixObjectsClass();
        mixObjectsClass.initValues();

        Bundle bundle = BundleUtils.convertToBundleReflection(mixObjectsClass);
        Configuration config = Configuration.fromBundle(bundle);

        Value referencedValue = Configuration.getReferencedValue(1, config.getContent());

        assertEquals(config.getContent(), referencedValue);
        assertNull(Configuration.getReferencedValue(2, config.getContent()));
    }

    public static class MixObjectsClass {
        @Description(description = "Une map de Integers")
        public Map<String, Integer> mapInteger;
        @Description(description = "Une map de Float")
        public List<Float> listFloat;
        @ClassValidator(predicateClass = CreditCardChecker.class)
        public Integer[] arrayIntegers;
        @ClassValidator(predicateClass = CreditCardChecker.class)
        @RangeValidator(minRange = 0, maxRange = 255)
        public byte octet;
        public boolean bool;
        public MixObjectsClass self;

        public MixObjectsClass() {
            this.mapInteger = new HashMap<>();
            this.listFloat = new ArrayList<>();
            this.arrayIntegers = new Integer[5];
        }

        public void initValues() {
            this.mapInteger.put("Premier", 5);
            this.mapInteger.put("Deuxieme", 256);
            this.mapInteger.put("Troisieme", 123);

            this.listFloat.add(12f);
            this.listFloat.add(0.3f);
            this.listFloat.add(789.54f);

            for (int i = 0; i < 5; i++) {
                this.arrayIntegers[i] = i;
            }

            this.octet = 100;
            this.bool = true;

            this.self = this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MixObjectsClass that = (MixObjectsClass) o;
            return octet == that.octet &&
                    bool == that.bool &&
                    Objects.equals(mapInteger, that.mapInteger) &&
                    Objects.equals(listFloat, that.listFloat) &&
                    Arrays.equals(arrayIntegers, that.arrayIntegers);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(mapInteger, listFloat, octet, bool);
            result = 31 * result + Arrays.hashCode(arrayIntegers);
            return result;
        }

        @Override
        public String toString() {
            return "MixObjectsClass{" +
                    "mapInteger=" + mapInteger +
                    ", listFloat=" + listFloat +
                    ", arrayIntegers=" + Arrays.toString(arrayIntegers) +
                    ", octet=" + octet +
                    ", bool=" + bool +
                    '}';
        }
    }
}
