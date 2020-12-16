package fr.uge.confroid.utils;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BundleUtilsTest {

    @Test
    public void objectToBundleTest() {
        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));

        Bundle bundle = BundleUtils.convertToBundleReflection(prefs);

        assertEquals(3, bundle.size());
        assertEquals(1, bundle.getInt(BundleUtils.ID_KEYWORD));
        assertEquals(ShoppingPreferences.class.getName(), bundle.getString(BundleUtils.CLASS_KEYWORD));

        assertEquals(3, bundle.getBundle("shoppingInfos").size());

        {
            Bundle workBundle = bundle.getBundle("shoppingInfos").getBundle("work");
            assertEquals(5, workBundle.size());
            assertEquals(2, workBundle.getInt(BundleUtils.ID_KEYWORD));
            assertEquals(ShoppingInfo.class.getName(), workBundle.getString(BundleUtils.CLASS_KEYWORD));
            assertFalse(workBundle.getBoolean("favorite"));

            {
                Bundle workBundleAddress = workBundle.getBundle("address");
                assertEquals(6, workBundleAddress.size());
                assertEquals(3, workBundleAddress.getInt(BundleUtils.ID_KEYWORD));
                assertEquals(ShippingAddress.class.getName(), workBundleAddress.getString(BundleUtils.CLASS_KEYWORD));
            }

            {
                Bundle workBundleBilling = workBundle.getBundle("billing");
                assertEquals(7, workBundleBilling.size());
                assertEquals(4, workBundleBilling.getInt(BundleUtils.ID_KEYWORD));
                assertEquals(BillingDetails.class.getName(), workBundleBilling.getString(BundleUtils.CLASS_KEYWORD));
                assertEquals("Bugdroid", workBundleBilling.getString("cardHolder"));
                assertEquals("123456789", workBundleBilling.getString("cardNumber"));
                assertEquals(12, workBundleBilling.getInt("expirationMonth"));
            }
        }

        {
            Bundle homeBundle = bundle.getBundle("shoppingInfos").getBundle("home");
            assertEquals(5, homeBundle.size());
            assertEquals(5, homeBundle.getInt(BundleUtils.ID_KEYWORD));
            assertEquals(ShoppingInfo.class.getName(), homeBundle.getString(BundleUtils.CLASS_KEYWORD));
            assertTrue(homeBundle.getBoolean("favorite"));

            {
                Bundle homeBundleAddress = homeBundle.getBundle("address");
                assertEquals(6, homeBundleAddress.size());
                assertEquals(6, homeBundleAddress.getInt(BundleUtils.ID_KEYWORD));
                assertEquals(ShippingAddress.class.getName(), homeBundleAddress.getString(BundleUtils.CLASS_KEYWORD));
            }

            {
                assertEquals(4, homeBundle.getBundle("billing").getInt(BundleUtils.REF_KEYWORD));
            }
        }
    }

    @Test
    public void bundleToObjectTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Bundle bundle = new Bundle();

        bundle.putInt(BundleUtils.ID_KEYWORD, 1);
        bundle.putString(BundleUtils.CLASS_KEYWORD, ShoppingPreferences.class.getName());
        Bundle shopInfos = new Bundle();
        shopInfos.putString(BundleUtils.CLASS_KEYWORD, HashMap.class.getName());
        bundle.putBundle("shoppingInfos", shopInfos);

        {
            Bundle home = new Bundle();
            shopInfos.putBundle("home", home);
            home.putInt(BundleUtils.ID_KEYWORD, 2);
            home.putString(BundleUtils.CLASS_KEYWORD, ShoppingInfo.class.getName());

            {
                Bundle address = new Bundle();
                home.putBundle("address", address);
                address.putInt(BundleUtils.ID_KEYWORD, 3);
                address.putString(BundleUtils.CLASS_KEYWORD, ShippingAddress.class.getName());
                address.putString("name", "Bugdroid");
                address.putString("street", "Bd Descartes");
                address.putString("city", "Champs-sur-Marne");
                address.putString("country", "France");
            }

            {
                Bundle billing = new Bundle();
                home.putBundle("billing", billing);
                billing.putInt(BundleUtils.ID_KEYWORD, 4);
                billing.putString(BundleUtils.CLASS_KEYWORD, BillingDetails.class.getName());
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
            work.putInt(BundleUtils.ID_KEYWORD, 5);
            work.putString(BundleUtils.CLASS_KEYWORD, ShoppingInfo.class.getName());

            {
                Bundle address = new Bundle();
                work.putBundle("address", address);
                address.putInt(BundleUtils.ID_KEYWORD, 6);
                address.putString(BundleUtils.CLASS_KEYWORD, ShippingAddress.class.getName());
                address.putString("name", "Bugdroid");
                address.putString("street", "Rue des tartes au nougat");
                address.putString("city", "Lollipop City");
                address.putString("country", "Oreo Country");
            }

            {
                Bundle billing = new Bundle();
                work.putBundle("billing", billing);
                billing.putInt(BundleUtils.REF_KEYWORD, 4);
            }

            work.putBoolean("favorite", false);
        }


        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));

        ShoppingPreferences prefsAfterConversion = (ShoppingPreferences) BundleUtils.convertFromBundle(bundle);
        assertEquals(prefs, prefsAfterConversion);
    }
}
