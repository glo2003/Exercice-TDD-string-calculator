package com.github.glo2003;

import java.util.Arrays;

public class Calculator {

    private static final String DEFAULT_DELIMITER = ",";

    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        try {
            return Arrays.stream(numbers.split(DEFAULT_DELIMITER))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .reduce(0, Integer::sum);
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }
}
