package com.imsweb.datagenerator.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RandomUtils {

    private static final Random _RANDOM = new Random();

    /**
     * Generates a random integer between 0 (inclusive) and the requested bound (exclusive).
     */
    public static int nextInt(int bound) {
        return _RANDOM.nextInt(bound);
    }

    /**
     * Generates a random integer between the two bounds (both inclusive).
     */
    public static int nextIntInRange(int minBound, int maxBound) {
        return _RANDOM.nextInt(maxBound - minBound + 1) + minBound;
    }

    /**
     * Generates a random double between 0.0 and 1.0.
     */
    public static double nextDouble() {
        return _RANDOM.nextDouble();
    }

    /**
     * Generates a random letter.
     */
    public static char nextLetter() {
        return (char)(nextInt(26) + 65);
    }

    /**
     * Generates a random digit as a character.
     */
    public static char nextDigit() {
        return (char)(nextInt(10) + 48);
    }

    /**
     * Generates a random string of characters.
     */
    public static String getRandomStringOfLetters(int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++)
            buf.append(nextLetter());
        return buf.toString();
    }

    /**
     * Generates a random string of digits.
     */
    public static String getRandomStringOfDigits(int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++)
            buf.append(nextDigit());
        return buf.toString();
    }

    /**
     * Generates a random string of characters and digits.
     */
    public static String getRandomStringOfLettersOrDigits(int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++)
            buf.append(nextInt(100) > 75 ? nextDigit() : nextLetter());
        return buf.toString();
    }

    /**
     * Returns a random date that is no earlier than the latest date in minDates and no later than the earliest date in maxDates
     */
    public static LocalDate getRandomDateBetween(Collection<LocalDate> minDates, Collection<LocalDate> maxDates) {
        if (minDates == null || minDates.isEmpty())
            throw new RuntimeException("At least one min date must be provided");
        if (maxDates == null || maxDates.isEmpty())
            throw new RuntimeException("At least one max date must be provided");
        return getRandomDateBetween(Collections.max(minDates), Collections.min(maxDates));
    }

    /**
     * Returns a random date between the two provided dates
     */
    public static LocalDate getRandomDateBetween(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null)
            throw new RuntimeException("Neither date may be null");
        int randomOffset = RandomUtils.nextIntInRange(0, Math.abs((int)ChronoUnit.DAYS.between(date1, date2)));
        return date1.isBefore(date2) ? date1.plusDays(randomOffset) : date2.plusDays(randomOffset);
    }
}
