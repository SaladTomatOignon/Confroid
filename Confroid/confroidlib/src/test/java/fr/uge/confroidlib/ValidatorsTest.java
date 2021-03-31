package fr.uge.confroidlib;

import org.junit.Test;

import fr.uge.confroidlib.validators.CreditCardChecker;

import static org.junit.Assert.*;

public class ValidatorsTest {

    @Test
    public void wrongCreditCardCheckerTest() {
        CreditCardChecker checker = new CreditCardChecker();

        assertFalse(checker.test("4515800825718557"));
        assertFalse(checker.test("4485929033160876"));
        assertFalse(checker.test("4200997456670238964"));
        assertFalse(checker.test("6011886314907243"));
        assertFalse(checker.test("6011754824471797"));
        assertFalse(checker.test("6011584419464989463"));
        assertFalse(checker.test("5893277036395989"));
        assertFalse(checker.test("3545738157342727"));

        assertFalse(checker.test(""));
        assertFalse(checker.test(" "));
        assertFalse(checker.test("a"));
        assertFalse(checker.test("5a"));
        assertFalse(checker.test("a5"));
        assertFalse(checker.test("789456a123"));
        assertFalse(checker.test(null));
    }

    @Test
    public void validCreditCardCheckerTest() {
        CreditCardChecker checker = new CreditCardChecker();

        assertTrue(checker.test("4556737586899855"));
    }
}
