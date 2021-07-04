package com.github.glo2003;

public class Calculator {
    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        throw new InvalidNumberFormatException();
    }
}
