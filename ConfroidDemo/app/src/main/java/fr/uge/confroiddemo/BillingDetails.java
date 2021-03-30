package fr.uge.confroiddemo;

import java.util.Objects;

import fr.uge.confroidlib.annotations.ClassValidator;
import fr.uge.confroidlib.annotations.Description;
import fr.uge.confroidlib.annotations.RangeValidator;
import fr.uge.confroidlib.annotations.RegexValidator;
import fr.uge.confroidlib.validators.CreditCardChecker;

public class BillingDetails {
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
