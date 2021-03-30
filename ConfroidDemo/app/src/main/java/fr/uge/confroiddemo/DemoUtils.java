package fr.uge.confroiddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoUtils {

    public static ShoppingPreferences createShoppingPreferences() {
        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.map("home", new ShoppingInfo(address1, billing, true));
        prefs.map("work", new ShoppingInfo(address2, billing, false));

        return prefs;
    }

    public static Map<String, Integer> createIntegerMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("K1", 10);
        map.put("K2", 20);

        return map;
    }

    public static Integer[] createIntegerArray() {
        return new Integer[]  { 10, 20, 30 };
    }

    public static List<Integer> createIntegerList() {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        lst.add(10);
        lst.add(20);

        return lst;
    }

    public static int createIntegerPrimitive() {
        return 10;
    }
}
