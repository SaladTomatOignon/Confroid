package fr.uge.confroid.configuration;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BundleToConfigurationTest {

    @Test
    public void bundleToConfig_nestedDictionary() {
        Bundle bundlePrimitive = new Bundle();
        bundlePrimitive.putString("primitive", "Hello");

        Bundle bundle1 = new Bundle();
        bundle1.putInt("0", 789);
        bundle1.putFloat("1", 12.45f);
        bundle1.putString("2", "Ok Many");

        Bundle bundle2 = new Bundle();
        bundle2.putInt("entier", 42);
        bundle2.putBoolean("bouboul", true);
        bundle2.putBundle("nested", bundle1);
        bundle2.putBundle("UnDernier", bundlePrimitive);

        Value config = Configuration.fromBundle(bundle2).getContent();

        assertTrue(config.isDictionary());
        assertEquals(42, (int) config.getMap().get("entier").getPrimitive().getInteger());
        assertTrue(config.getMap().get("nested").isArray());
        assertEquals("Ok Many", config.getMap().get("nested").getArray()[2].getPrimitive().getString());
        assertTrue(config.getMap().get("UnDernier").isPrimitive());
        assertEquals("Hello", config.getMap().get("UnDernier").getPrimitive().getString());
    }
}
