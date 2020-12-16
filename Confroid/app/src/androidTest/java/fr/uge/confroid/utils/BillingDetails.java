package fr.uge.confroid.utils;

import java.util.Objects;

public class BillingDetails {
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
