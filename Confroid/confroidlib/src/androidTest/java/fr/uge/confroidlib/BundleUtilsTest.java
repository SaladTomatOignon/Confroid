package fr.uge.confroidlib;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.Description;
import fr.uge.confroidlib.annotations.GeoCoordinates;
import fr.uge.confroidlib.annotations.PhoneNumber;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.annotations.RegexValidator;
import fr.uge.confroidlib.validators.CreditCardChecker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BundleUtilsTest {

    @Test
    public void IntegerTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        int integer = 15;

        Bundle bundle = BundleUtils.convertToBundleReflection(integer);
        int convertedInteger = (int) BundleUtils.convertFromBundle(bundle);

        assertEquals(integer, convertedInteger);
    }

    @Test
    public void StringTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String string = "Salut";

        Bundle bundle = BundleUtils.convertToBundleReflection(string);
        String convertedString = (String) BundleUtils.convertFromBundle(bundle);

        assertEquals(string, convertedString);
    }

    @Test
    public void stringMapTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        HashMap<String, String> map = new HashMap<>();
        map.put("Salut", "valeur1");
        map.put("Coco", "valeur2");
        map.put("Mdr", "valeur3");

        Bundle bundle = BundleUtils.convertToBundleReflection(map);
        HashMap<String, String> convertedMap = (HashMap<String, String>) BundleUtils.convertFromBundle(bundle);

        assertEquals(map, convertedMap);
    }

    @Test
    public void intMapTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Salut", 1);
        map.put("Coco", 2);
        map.put("Mdr", 3);

        Bundle bundle = BundleUtils.convertToBundleReflection(map);
        HashMap<String, Integer> convertedMap = (HashMap<String, Integer>) BundleUtils.convertFromBundle(bundle);

        assertEquals(map, convertedMap);
    }

    @Test
    public void billingDetailsMapTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        HashMap<String, BillingDetails> map = new HashMap<>();
        map.put("Salut", new BillingDetails("Bugdroid", "123456789", 12, 2021, 123));
        map.put("Coco", new BillingDetails("James bond", "987654321", 10, 2025, 159));
        map.put("Mdr", new BillingDetails("Bruce Wayne", "789456123", 5, 2032, 147));

        Bundle bundle = BundleUtils.convertToBundleReflection(map);
        HashMap<String, BillingDetails> convertedMap = (HashMap<String, BillingDetails>) BundleUtils.convertFromBundle(bundle);

        assertEquals(map, convertedMap);
    }

    @Test
    public void stringListTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        List<String> lst = new ArrayList<>();
        lst.add("Salut");
        lst.add("Coco");
        lst.add("Mdr");

        Bundle bundle = BundleUtils.convertToBundleReflection(lst);
        List<String> convertedList = (ArrayList<String>) BundleUtils.convertFromBundle(bundle);

        assertEquals(lst, convertedList);
    }

    @Test
    public void intListTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        List<Integer> lst = new ArrayList<>();
        lst.add(1);
        lst.add(55);
        lst.add(94);

        Bundle bundle = BundleUtils.convertToBundleReflection(lst);
        List<Integer> convertedList = (ArrayList<Integer>) BundleUtils.convertFromBundle(bundle);

        assertEquals(lst, convertedList);
    }

    @Test
    public void billingDetailsListTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        List<BillingDetails> lst = new ArrayList<>();
        lst.add(new BillingDetails("Bugdroid", "123456789", 12, 2021, 123));
        lst.add(new BillingDetails("James bond", "987654321", 10, 2025, 159));
        lst.add(new BillingDetails("Bruce Wayne", "789456123", 5, 2032, 147));

        Bundle bundle = BundleUtils.convertToBundleReflection(lst);
        List<BillingDetails> convertedList = (ArrayList<BillingDetails>) BundleUtils.convertFromBundle(bundle);

        assertEquals(lst, convertedList);
    }

    @Test
    public void stringArrayTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String[] array = new String[] {"Salut", "Coco", "Mdr"};

        Bundle bundle = BundleUtils.convertToBundleReflection(array);
        String[] convertedList = (String[]) BundleUtils.convertFromBundle(bundle);

        assertArrayEquals(array, convertedList);
    }

    @Test
    public void intArrayTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        int[] array = new int[] {42, 789, -123, 0};

        Bundle bundle = BundleUtils.convertToBundleReflection(array);
        int[] convertedList = (int[]) BundleUtils.convertFromBundle(bundle);

        assertArrayEquals(array, convertedList);
    }

    @Test
    public void floatArrayTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        float[] array = new float[] {42.25f, 789.807f, -123f, 0.1f};

        Bundle bundle = BundleUtils.convertToBundleReflection(array);
        float[] convertedList = (float[]) BundleUtils.convertFromBundle(bundle);

        assertArrayEquals(array, convertedList, 0.001f);
    }

    @Test
    public void integerArrayTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Integer[] array = new Integer[] {42, 789, -123, 0};

        Bundle bundle = BundleUtils.convertToBundleReflection(array);
        Integer[] convertedList = (Integer[]) BundleUtils.convertFromBundle(bundle);

        assertArrayEquals(array, convertedList);
    }

    @Test
    public void billingDetailsArrayTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        BillingDetails[] array = new BillingDetails[] {
                new BillingDetails("Bugdroid", "123456789", 12, 2021, 123),
                new BillingDetails("James bond", "987654321", 10, 2025, 159),
                new BillingDetails("Bruce Wayne", "789456123", 5, 2032, 147)};

        Bundle bundle = BundleUtils.convertToBundleReflection(array);
        BillingDetails[] convertedList = (BillingDetails[]) BundleUtils.convertFromBundle(bundle);

        assertArrayEquals(array, convertedList);
    }

    @Test
    public void MixObjectsClassTest() throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        MixObjectsClass mixObjectsClass = new MixObjectsClass();
        mixObjectsClass.initValues();

        Bundle bundle = BundleUtils.convertToBundleReflection(mixObjectsClass);

        MixObjectsClass convertedObject = (MixObjectsClass) BundleUtils.convertFromBundle(bundle);

        assertEquals(mixObjectsClass, convertedObject);
    }

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

    @Test
    public void objectAnnotationToBundleTest() {
        BillingDetailsWithAnnotations billing = new BillingDetailsWithAnnotations("Bugdroid", "123456789", 12, 2021, 123);

        Bundle bundle = BundleUtils.convertToBundleReflection(billing);

        assertEquals(18, bundle.size());

        // Testing Description annotation from cardHolder field
        {
            Bundle descAnnotBundle = bundle.getBundle("cardHolder" + BundleUtils.ANNOTATION_SEP + Description.class.getSimpleName());
            assertEquals(1, descAnnotBundle.size());
            assertEquals("Titulaire de la carte", descAnnotBundle.getString(BundleUtils.ANNOTATION_PARAM + "1"));
        }

        // Testing RegexValidator annotation from cardNumber field
        {
            Bundle regexAnnotBundle = bundle.getBundle("cardHolder" + BundleUtils.ANNOTATION_SEP + RegexValidator.class.getSimpleName());
            assertEquals(1, regexAnnotBundle.size());
            assertEquals(".+", regexAnnotBundle.getString(BundleUtils.ANNOTATION_PARAM + "1"));
        }

        // Testing ClassValidator annotation from cardNumber field
        {
            Bundle classValidatorAnnotBundle = bundle.getBundle("cardNumber" + BundleUtils.ANNOTATION_SEP + ClassValidator.class.getSimpleName());
            assertEquals(1, classValidatorAnnotBundle.size());
            assertEquals("CreditCardChecker", classValidatorAnnotBundle.getString(BundleUtils.ANNOTATION_PARAM + "1"));
        }

        // Testing RangeValidator annotation from expirationYear field
        {
            Bundle rangeValidatorAnnotBundle = bundle.getBundle("expirationYear" + BundleUtils.ANNOTATION_SEP + RangeValidator.class.getSimpleName());
            assertEquals(2, rangeValidatorAnnotBundle.size());
            assertEquals(2020, rangeValidatorAnnotBundle.getLong(BundleUtils.ANNOTATION_PARAM + "1"));
            assertEquals(2040, rangeValidatorAnnotBundle.getLong(BundleUtils.ANNOTATION_PARAM + "2"));
        }

        // Testing Description annotation from cryptogram field
        {
            Bundle descAnnotBundle = bundle.getBundle("cryptogram" + BundleUtils.ANNOTATION_SEP + Description.class.getSimpleName());
            assertEquals(1, descAnnotBundle.size());
            assertEquals("cryptogram", descAnnotBundle.getString(BundleUtils.ANNOTATION_PARAM + "1"));
        }
    }

    @Test
    public void bundleToObjectAnnotationTest() {
        Bundle description = new Bundle();
        Bundle descriptionParams = new Bundle();
        descriptionParams.putString(BundleUtils.ANNOTATION_PARAM + "1", "Salut ca va");
        description.putBundle("field" + BundleUtils.ANNOTATION_SEP + Description.class.getSimpleName(), descriptionParams);

        Bundle regexValidor = new Bundle();
        Bundle regexValidorParams = new Bundle();
        regexValidorParams.putString(BundleUtils.ANNOTATION_PARAM + "1", "[0-9]{16}");
        regexValidor.putBundle("field" + BundleUtils.ANNOTATION_SEP + RegexValidator.class.getSimpleName(), regexValidorParams);

        Bundle classValidator = new Bundle();
        Bundle classValidatorParams = new Bundle();
        classValidatorParams.putString(BundleUtils.ANNOTATION_PARAM + "1", CreditCardChecker.class.getSimpleName());
        classValidator.putBundle("field" + BundleUtils.ANNOTATION_SEP + ClassValidator.class.getSimpleName(), classValidatorParams);

        Bundle rangeValidator = new Bundle();
        Bundle rangeValidatorParams = new Bundle();
        rangeValidatorParams.putLong(BundleUtils.ANNOTATION_PARAM + "1", 15);
        rangeValidatorParams.putLong(BundleUtils.ANNOTATION_PARAM + "2", 94);
        rangeValidator.putBundle("field" + BundleUtils.ANNOTATION_SEP + RangeValidator.class.getSimpleName(), rangeValidatorParams);

        Description descAnnot = (Description) BundleUtils.getAnnotationFromBundle(description);
        RegexValidator regexValidatorAnnot = (RegexValidator) BundleUtils.getAnnotationFromBundle(regexValidor);
        ClassValidator classValidatorAnnot = (ClassValidator) BundleUtils.getAnnotationFromBundle(classValidator);
        RangeValidator rangeValidatorAnnot = (RangeValidator) BundleUtils.getAnnotationFromBundle(rangeValidator);
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

    static class BillingDetailsWithAnnotations {

        @Description(description = "Titulaire de la carte")
        @RegexValidator(pattern = ".+")
        public String cardHolder;

        @Description(description = "Numéro de carte")
        @RegexValidator(pattern = "[0-9]{16}")
        @ClassValidator(predicateClass = CreditCardChecker.class)
        public String cardNumber;

        @Description(description = "Mois d'expiration")
        @RangeValidator(minRange = 1, maxRange = 12)
        public int expirationMonth;

        @Description(description = "Année d'expiration")
        @RangeValidator(minRange = 2020, maxRange = 2040)
        public int expirationYear;

        @Description()
        @RangeValidator(minRange = 0, maxRange = 999)
        public int cryptogram;

        public BillingDetailsWithAnnotations(String cardHolder, String cardNumber, int expirationMonth, int expirationYear, int cryptogram) {
            this.cardHolder = cardHolder;
            this.cardNumber = cardNumber;
            this.expirationMonth = expirationMonth;
            this.expirationYear = expirationYear;
            this.cryptogram = cryptogram;
        }

        public BillingDetailsWithAnnotations() {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BillingDetailsWithAnnotations that = (BillingDetailsWithAnnotations) o;
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

    static class MixObjectsClass {
        @Description(description = "Une map de Integers")
        public Map<String, Integer> mapInteger;
        public Map<String, ShippingAddress> mapShippingAddress;
        @Description(description = "Une map de Float")
        public List<Float> listFloat;
        public List<ShippingAddress> listShippingAddress;
        @ClassValidator(predicateClass = CreditCardChecker.class)
        public Integer[] arrayIntegers;
        public ShippingAddress[] arrayShippingAddress;
        @ClassValidator(predicateClass = CreditCardChecker.class)
        @RangeValidator(minRange = 0, maxRange = 255)
        public byte octet;
        public boolean bool;
        public MixObjectsClass self;
        @GeoCoordinates
        public float[] gpsCoordinates;
        @PhoneNumber
        public String phoneNumber;

        public MixObjectsClass() {
            this.mapInteger = new HashMap<>();
            this.mapShippingAddress = new HashMap<>();
            this.listFloat = new ArrayList<>();
            this.listShippingAddress = new ArrayList<>();
            this.arrayIntegers = new Integer[5];
            this.arrayShippingAddress = new ShippingAddress[3];
        }

        public void initValues() {
            this.mapInteger.put("Premier", 5);
            this.mapInteger.put("Deuxieme", 256);
            this.mapInteger.put("Troisieme", 123);

            this.mapShippingAddress.put("Premier", new ShippingAddress("Name", "Street", "City", "Country"));
            this.mapShippingAddress.put("Deuxieme", new ShippingAddress("Nom", "Rue", "Ville", "Pays"));

            this.listFloat.add(12f);
            this.listFloat.add(0.3f);
            this.listFloat.add(789.54f);

            this.listShippingAddress.add(new ShippingAddress("Name", "Street", "City", "Country"));
            this.listShippingAddress.add(new ShippingAddress("Nom", "Rue", "Ville", "Pays"));

            for (int i = 0; i < 5; i++) {
                this.arrayIntegers[i] = i;
            }

            for (int i = 0; i < 3; i++) {
                this.arrayShippingAddress[i] = new ShippingAddress("Name" + i, "Street" + i, "City" + i, "Country" + i);
            }

            this.octet = 100;
            this.bool = true;

            this.self = this;

            this.gpsCoordinates = new float[] {48.8391196f, 2.5870128f, 99f};
            this.phoneNumber = "0651487597";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MixObjectsClass that = (MixObjectsClass) o;
            return octet == that.octet &&
                    bool == that.bool &&
                    mapInteger.equals(that.mapInteger) &&
                    mapShippingAddress.equals(that.mapShippingAddress) &&
                    listFloat.equals(that.listFloat) &&
                    listShippingAddress.equals(that.listShippingAddress) &&
                    Arrays.equals(arrayIntegers, that.arrayIntegers) &&
                    Arrays.equals(arrayShippingAddress, that.arrayShippingAddress);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(mapInteger, mapShippingAddress, listFloat, listShippingAddress, octet, bool);
            result = 31 * result + Arrays.hashCode(arrayIntegers);
            result = 31 * result + Arrays.hashCode(arrayShippingAddress);
            return result;
        }

        @Override
        public String toString() {
            return "MixObjectsClass{" +
                    "mapInteger=" + mapInteger +
                    ", mapShippingAddress=" + mapShippingAddress +
                    ", listFloat=" + listFloat +
                    ", listShippingAddress=" + listShippingAddress +
                    ", arrayIntegers=" + Arrays.toString(arrayIntegers) +
                    ", arrayShippingAddress=" + Arrays.toString(arrayShippingAddress) +
                    ", octet=" + octet +
                    ", bool=" + bool +
                    '}';
        }
    }
}
