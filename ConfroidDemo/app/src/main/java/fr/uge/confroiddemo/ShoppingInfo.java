package fr.uge.confroiddemo;

import java.util.Objects;

public class ShoppingInfo {
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
