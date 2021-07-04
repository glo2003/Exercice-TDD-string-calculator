package com.github.glo2003;

import java.util.List;

public class NegativeNumberException extends RuntimeException {
    NegativeNumberException(List<Integer> numbers) {
        super("Some negative numbers where encountered: " + numbers.stream()
                .map(String::valueOf)
                .reduce("", (x, y) -> x + ", " + y));
    }
}
