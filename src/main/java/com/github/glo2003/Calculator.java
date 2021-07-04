package com.github.glo2003;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Calculator {

    private static final String DEFAULT_DELIMITER = ",";
    private static final String DELIMITER_SECTION_START = "//";
    public static final String DELIMITER_END = "\n";
    private static final char BRACKETED_DELIMITER_START = '[';
    private static final char BRACKETED_DELIMITER_END = ']';
    private static final int MAX_NUMBER = 1000;

    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        List<String> delimiterSectionAndBody = splitDelimiterSectionAndBody(numbers);
        String delimiterSection = delimiterSectionAndBody.get(0);
        String body = delimiterSectionAndBody.get(1);

        String delimiterRegex = parseDelimiters(delimiterSection);

        List<Integer> parsedNumbers = parseNumbers(body, delimiterRegex);
        checkNegativeNumbers(parsedNumbers);

        return filterNumbers(parsedNumbers).reduce(0, Integer::sum);
    }

    private List<String> splitDelimiterSectionAndBody(String input) {
        if (input.startsWith(DELIMITER_SECTION_START)) {
            int endDelimiterSection = input.indexOf(DELIMITER_END);
            return new ArrayList<String>() {{
                add(input.substring(0, endDelimiterSection + 1));
                add(input.substring(endDelimiterSection));
            }};
        }
        return new ArrayList<String>() {{
            add("");
            add(input);
        }};
    }

    private String parseDelimiters(String delimiterSection) {
        String delimiterText = delimiterSection;
        List<String> delimiters = new ArrayList<>();

        if (delimiterText.startsWith(DELIMITER_SECTION_START)) {
            delimiterText = delimiterText.substring(DELIMITER_SECTION_START.length());

            if (delimiterText.charAt(0) == BRACKETED_DELIMITER_START) {
                do {
                    delimiters.add(parseBracketedDelimiter(delimiterText));
                    int endDelimiter = delimiterText.indexOf(BRACKETED_DELIMITER_END);
                    delimiterText = delimiterText.substring(endDelimiter + 1);
                } while (delimiterText.startsWith(String.valueOf(BRACKETED_DELIMITER_START)));
            } else {
                String delimiter = parseSingleCharacterDelimiter(delimiterText);
                delimiters.add(delimiter);
            }
        } else {
            delimiters.add(DEFAULT_DELIMITER);
        }

        return makeDelimiterRegex(delimiters);
    }

    private String parseSingleCharacterDelimiter(String delimiterText) {
        int endDelimiterSection = delimiterText.indexOf(DELIMITER_END);
        return delimiterText.substring(0, endDelimiterSection);
    }

    private String parseBracketedDelimiter(String delimiterText) {
        int startDelimiter = 1;
        int endDelimiter = delimiterText.indexOf(BRACKETED_DELIMITER_END);
        return delimiterText.substring(startDelimiter, endDelimiter);
    }

    private String makeDelimiterRegex(List<String> delimiters) {
        return "\n" + delimiters.stream().reduce("", (x, y) -> x + "|\\Q" + y + "\\E");
    }

    private List<Integer> parseNumbers(String body, String delimiterRegex) {
        try {
            return Arrays.stream(body.split(delimiterRegex))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).collect(Collectors.toList());
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }

    private void checkNegativeNumbers(List<Integer> parsedNumbers) {
        Predicate<Integer> isNegative = x -> x < 0;
        boolean hasNegatives = parsedNumbers.stream().anyMatch(isNegative);
        if (hasNegatives) {
            List<Integer> negatives = parsedNumbers.stream()
                    .filter(isNegative)
                    .collect(Collectors.toList());
            throw new NegativeNumberException(negatives);
        }
    }

    private Stream<Integer> filterNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(x -> x <= MAX_NUMBER);
    }
}
