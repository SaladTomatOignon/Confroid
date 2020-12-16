package fr.uge.confroid.utils;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExtensionsTest {

    @Test
    public void objectToBundleTest() {
        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));

        Bundle bundle = Extensions.convertToBundleReflection(prefs);

        assertEquals(3, bundle.size());
        assertEquals(1, bundle.getInt(Extensions.ID_KEYWORD));
        assertEquals(ShoppingPreferences.class.getName(), bundle.getString(Extensions.CLASS_KEYWORD));

        assertEquals(3, bundle.getBundle("shoppingInfos").size());

        {
            Bundle workBundle = bundle.getBundle("shoppingInfos").getBundle("work");
            assertEquals(5, workBundle.size());
            assertEquals(2, workBundle.getInt(Extensions.ID_KEYWORD));
            assertEquals(ShoppingInfo.class.getName(), workBundle.getString(Extensions.CLASS_KEYWORD));
            assertFalse(workBundle.getBoolean("favorite"));

            {
                Bundle workBundleAddress = workBundle.getBundle("address");
                assertEquals(6, workBundleAddress.size());
                assertEquals(3, workBundleAddress.getInt(Extensions.ID_KEYWORD));
                assertEquals(ShippingAddress.class.getName(), workBundleAddress.getString(Extensions.CLASS_KEYWORD));
            }

            {
                Bundle workBundleBilling = workBundle.getBundle("billing");
                assertEquals(7, workBundleBilling.size());
                assertEquals(4, workBundleBilling.getInt(Extensions.ID_KEYWORD));
                assertEquals(BillingDetails.class.getName(), workBundleBilling.getString(Extensions.CLASS_KEYWORD));
                assertEquals("Bugdroid", workBundleBilling.getString("cardHolder"));
                assertEquals("123456789", workBundleBilling.getString("cardNumber"));
                assertEquals(12, workBundleBilling.getInt("expirationMonth"));
            }
        }

        {
            Bundle homeBundle = bundle.getBundle("shoppingInfos").getBundle("home");
            assertEquals(5, homeBundle.size());
            assertEquals(5, homeBundle.getInt(Extensions.ID_KEYWORD));
            assertEquals(ShoppingInfo.class.getName(), homeBundle.getString(Extensions.CLASS_KEYWORD));
            assertTrue(homeBundle.getBoolean("favorite"));

            {
                Bundle homeBundleAddress = homeBundle.getBundle("address");
                assertEquals(6, homeBundleAddress.size());
                assertEquals(6, homeBundleAddress.getInt(Extensions.ID_KEYWORD));
                assertEquals(ShippingAddress.class.getName(), homeBundleAddress.getString(Extensions.CLASS_KEYWORD));
            }

            {
                assertEquals(4, homeBundle.getBundle("billing").getInt(Extensions.REF_KEYWORD));
            }
        }
    }

    @Test
    public void bundleToObjectTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Bundle bundle = new Bundle();

        bundle.putInt(Extensions.ID_KEYWORD, 1);
        bundle.putString(Extensions.CLASS_KEYWORD, ShoppingPreferences.class.getName());
        Bundle shopInfos = new Bundle();
        shopInfos.putString(Extensions.CLASS_KEYWORD, HashMap.class.getName());
        bundle.putBundle("shoppingInfos", shopInfos);

        {
            Bundle home = new Bundle();
            shopInfos.putBundle("home", home);
            home.putInt(Extensions.ID_KEYWORD, 2);
            home.putString(Extensions.CLASS_KEYWORD, ShoppingInfo.class.getName());

            {
                Bundle address = new Bundle();
                home.putBundle("address", address);
                address.putInt(Extensions.ID_KEYWORD, 3);
                address.putString(Extensions.CLASS_KEYWORD, ShippingAddress.class.getName());
                address.putString("name", "Bugdroid");
                address.putString("street", "Bd Descartes");
                address.putString("city", "Champs-sur-Marne");
                address.putString("country", "France");
            }

            {
                Bundle billing = new Bundle();
                home.putBundle("billing", billing);
                billing.putInt(Extensions.ID_KEYWORD, 4);
                billing.putString(Extensions.CLASS_KEYWORD, BillingDetails.class.getName());
                billing.putString("cardHolder", "Bugdroid");
                billing.putString("cardNumber", "123456789");
                billing.putInt("expirationMonth", 12);
                billing.putInt("expirationYear", 2021);
                billing.putInt("cryptogram", 123);
            }

            home.putBoolean("favorite", true);
        }

        {
            Bundle work = new Bundle();
            shopInfos.putBundle("work", work);
            work.putInt(Extensions.ID_KEYWORD, 5);
            work.putString(Extensions.CLASS_KEYWORD, ShoppingInfo.class.getName());

            {
                Bundle address = new Bundle();
                work.putBundle("address", address);
                address.putInt(Extensions.ID_KEYWORD, 6);
                address.putString(Extensions.CLASS_KEYWORD, ShippingAddress.class.getName());
                address.putString("name", "Bugdroid");
                address.putString("street", "Rue des tartes au nougat");
                address.putString("city", "Lollipop City");
                address.putString("country", "Oreo Country");
            }

            {
                Bundle billing = new Bundle();
                work.putBundle("billing", billing);
                billing.putInt(Extensions.REF_KEYWORD, 4);
            }

            work.putBoolean("favorite", false);
        }


        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));

        ShoppingPreferences prefsAfterConversion = (ShoppingPreferences) Extensions.convertFromBundle(bundle);
        assertEquals(prefs, prefsAfterConversion);
    }
}
