package com.imsweb.datagenerator.random;

import java.util.Random;

import org.joda.time.LocalDate;

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
     * Generates a random year.
     */
    public static int getRandomYear(int min, int max) {
        return nextIntInRange(min, max);
    }

    /**
     * Generates a random month.
     */
    public static int getRandomMonth() {
        return nextIntInRange(1, 12);
    }

    /**
     * Generates a random day for a given year/month.
     */
    public static int getRandomDay(int year, int month) {
        return nextIntInRange(1, new LocalDate(year, month, 1).dayOfMonth().getMaximumValue());
    }
}
