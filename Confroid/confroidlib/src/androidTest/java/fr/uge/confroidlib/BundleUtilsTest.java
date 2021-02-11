package fr.uge.confroidlib;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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


    static class BillingDetails {
        public String cardHolder;
        public String cardNumber;
        public int expirationMonth;
        public int expirationYear;
        public int cryptogram;

        public BillingDetails(String cardHolder, String cardNumber, int expirationMonth, int expirationYear, int cryptogram) {
            this.cardHolder = cardHolder;
            this.cardNumber = cardNumber;
            this.expirationMonth = expirationMonth;
            this.expirationYear = expirationYear;
            this.cryptogram = cryptogram;
        }

        public BillingDetails() {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BillingDetails that = (BillingDetails) o;
            return expirationMonth == that.expirationMonth &&
                    expirationYear == that.expirationYear &&
                    cryptogram == that.cryptogram &&
                    Objects.equals(cardHolder, that.cardHolder) &&
                    Objects.equals(cardNumber, that.cardNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cardHolder, cardNumber, expirationMonth, expirationYear, cryptogram);
        }
    }

    static class ShippingAddress {
        public String name;
        public String street;
        public String city;
        public String country;

        public ShippingAddress(String name, String street, String city, String country) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.country = country;
        }

        public ShippingAddress() {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShippingAddress that = (ShippingAddress) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(street, that.street) &&
                    Objects.equals(city, that.city) &&
                    Objects.equals(country, that.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, street, city, country);
        }
    }

    static class ShoppingInfo {
        public ShippingAddress address;
        public BillingDetails billing;
        public boolean favorite;

        public ShoppingInfo(ShippingAddress address, BillingDetails billing, boolean favorite) {
            this.address = address;
            this.billing = billing;
            this.favorite = favorite;
        }

        public ShoppingInfo() {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShoppingInfo that = (ShoppingInfo) o;
            return favorite == that.favorite &&
                    Objects.equals(address, that.address) &&
                    Objects.equals(billing, that.billing);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address, billing, favorite);
        }
    }

    static class ShoppingPreferences {
        public Map<String, ShoppingInfo> shoppingInfos;

        public ShoppingPreferences() {
            this.shoppingInfos = new HashMap<>();
        }

        public void map(String name, ShoppingInfo shopInfo) {
            shoppingInfos.put(name, shopInfo);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShoppingPreferences that = (ShoppingPreferences) o;
            return shoppingInfos.equals(that.shoppingInfos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shoppingInfos);
        }
    }
}
