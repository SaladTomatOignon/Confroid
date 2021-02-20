package fr.uge.confroidlib.validators;

import java.util.function.Predicate;

public class CreditCardChecker implements Predicate<String> {
    @Override
    public boolean test(String creditCard) {
        return true;
    }
}
