package fr.uge.confroidlib.validators;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreditCardChecker implements Predicate<String> {

    @Override
    public boolean test(String creditCard) {
        if (Objects.isNull(creditCard) || creditCard.isEmpty()) {
            return false;
        }

        char[] array = creditCard.toCharArray();
        List<Integer> numbers;

        try {
            // Converting char array to int list
            numbers = IntStream.range(0, array.length)
                    .mapToObj(i -> Integer.valueOf(String.valueOf(array[i])))
                    .collect(Collectors.toList());
        } catch (NumberFormatException nfe) {
            return false;
        }

        return luhnFormula(numbers);
    }

    /**
     * Determines if the given succession of digits respects the Luhn formula
     *
     * @param creditCard The list of digits constituting a credit card
     * @return True if the digits list valid, false otherwise
     */
    private boolean luhnFormula(List<Integer> creditCard) {
        // Removing last digit
        int lastDigit = creditCard.remove(creditCard.size() - 1);

        // Reversing the digits
        Collections.reverse(creditCard);

        // Multiplying the digits in even positions (0, 2, 4, etc.) by 2 and subtract 9 to all any result higher than 9
        // Then adding all the numbers together
        OptionalInt sum = IntStream.range(0, creditCard.size()).map(i -> {
            int result = creditCard.get(i);

            if (i % 2 == 0) {
                result = creditCard.get(i) * 2;
            }

            if (result > 9) {
                result = result - 9;
            }

            return result;
        }).reduce(Integer::sum);

        // If the sum modulo 10 equals the first digit, then it's validated
        if (sum.isPresent()) {
            return sum.getAsInt() % 10 == lastDigit;
        } else {
            return false;
        }
    }
}
