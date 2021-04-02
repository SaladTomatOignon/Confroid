package fr.uge.confroid.front;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.uge.confroidlib.ConfroidUtils;
import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.Description;
import fr.uge.confroidlib.annotations.GeoCoordinates;
import fr.uge.confroidlib.annotations.PhoneNumber;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.annotations.RegexValidator;
import fr.uge.confroidlib.validators.CreditCardChecker;

public class Demo {
    public static void create(Context context) {
        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));
        ConfroidUtils.saveConfiguration(context, "shoppingPreferences", prefs, "stable");


        ConfroidUtils.saveConfiguration(context, "array", new Integer[]  { 10, 20, 30 }, null);
        ConfroidUtils.saveConfiguration(context, "primitive", 10, null);

        HashMap<String, Integer> map = new HashMap();
        map.put("K1", 10);
        map.put("K2", 20);

        ConfroidUtils.saveConfiguration(context, "hashmap", map, null);


        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(10);
        list.add(20);
        ConfroidUtils.saveConfiguration(context, "list", list, null);

        Contact contact = new Contact("0623465179");
        ConfroidUtils.saveConfiguration(context, "contact", contact, "stable");

        Location location = new Location(48.8706298f, 2.3169336f);
        ConfroidUtils.saveConfiguration(context, "location", location, "stable");
    }

    public static class Contact {
        @PhoneNumber
        public String phoneNumber;

        public Contact() {}

        public Contact(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Contact contact = (Contact) o;
            return Objects.equals(phoneNumber, contact.phoneNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(phoneNumber);
        }
    }

    public static class Location {
        @GeoCoordinates
        public float[] coordinates;

        public Location(float latitude, float longitude) {
            coordinates = new float[] { latitude, longitude };
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return Arrays.equals(coordinates, location.coordinates);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(coordinates);
        }
    }
    public static class BillingDetails {

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

    public static class ShippingAddress {
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

    public static class ShoppingInfo {
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

    public static class ShoppingPreferences {
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
