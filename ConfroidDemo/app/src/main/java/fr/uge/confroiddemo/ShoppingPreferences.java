package fr.uge.confroiddemo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShoppingPreferences {
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
