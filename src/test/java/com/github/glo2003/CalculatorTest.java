package com.github.glo2003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

    Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // step 1
    @Test
    void whenEmptyString_thenReturnsZero() {
        int result = calculator.add("");

        assertEquals(0, result);
    }

    @Test
    void whenOneNumber_thenReturnsThatNumber() {
        int result = calculator.add("1");

        assertEquals(1, result);
    }

    @Test
    void whenTwoNumbers_thenReturnsSum() {
        int result = calculator.add("1,2");

        assertEquals(3, result);
    }

    @Test
    void whenTrailingDelimiter_thenReturnsSum() {
        int result = calculator.add("1,");

        assertEquals(1, result);
    }

    @Test
    void whenDuplicatedDelimiters_thenReturnsSum() {
        int result = calculator.add("1,,2");

        assertEquals(3, result);
    }

    @Test
    void whenNonNumericValues_thenThrowInvalidInput() {
        assertThrows(InvalidNumberFormatException.class,
                () -> calculator.add("4,a"));
    }

    // Step 2
    @Test
    void whenMultipleNumbers_thenReturnsSum() {
        int result = calculator.add("1,2,3,4,5");

        assertEquals(15, result);
    }

    // Step 3
    @Test
    void whenNewLineSeparator_thenReturnsSum() {
        int result = calculator.add("1\n2,3");

        assertEquals(6, result);
    }

    @Test
    void whenMissingNumberWithNewLine_thenIsValid() {
        int result = calculator.add("1,\n");

        assertEquals(1, result);
    }

    // Step 4
    @Test
    void whenChangeDelimiter_thenReturnsSum() {
        int result = calculator.add("//;\n1;2");

        assertEquals(3, result);
    }

    // Step 5
    @Test
    void whenNegativeNumbers_thenThrow() {
        assertThrows(NegativeNumberException.class,
                () -> calculator.add("-1,2"));
    }
}