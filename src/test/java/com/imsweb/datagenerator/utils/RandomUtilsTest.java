package com.imsweb.datagenerator.utils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void testNextInt() {
        int i = RandomUtils.nextInt(10);
        Assert.assertTrue(i >= 0 && i < 10);

        i = RandomUtils.nextInt(100);
        Assert.assertTrue(i >= 0 && i < 100);
    }

    @Test
    public void testNextDouble() {
        double d = RandomUtils.nextDouble();
        Assert.assertTrue(d >= 0.0 && d <= 1.0);
    }

    @Test
    public void testGetRandomDateBetween() {
        LocalDate date1 = LocalDate.of(1999, 12, 31);
        LocalDate date2 = LocalDate.of(2000, 1, 1);
        LocalDate date3 = LocalDate.of(2000, 1, 2);
        LocalDate date4 = LocalDate.of(2000, 1, 3);
        LocalDate date5 = LocalDate.of(2000, 1, 4);

        // date1 is the entire range
        Assert.assertEquals(date1, RandomUtils.getRandomDateBetween(Collections.singleton(date1), Collections.singleton(date1)));
        // date3 should be the min and the max
        Assert.assertEquals(date3, RandomUtils.getRandomDateBetween(new HashSet<>(Arrays.asList(date3, date1, date2)), new HashSet<>(Arrays.asList(date3, date4))));
        // date3 should be min, and date4 is max
        Assert.assertTrue(
                new HashSet<>(Arrays.asList(date3, date4)).contains(RandomUtils.getRandomDateBetween(new HashSet<>(Arrays.asList(date1, date3)), new HashSet<>(Arrays.asList(date5, date4)))));

        // date1 is the entire range
        Assert.assertEquals(date1, RandomUtils.getRandomDateBetween(date1, date1));
        // date1 or date2 are valid
        Assert.assertTrue(new HashSet<>(Arrays.asList(date1, date2)).contains(RandomUtils.getRandomDateBetween(date1, date2)));
        // date2 or date 3 are valid
        Assert.assertTrue(new HashSet<>(Arrays.asList(date2, date3)).contains(RandomUtils.getRandomDateBetween(date3, date2)));
    }

    @Test
    public void testGetRandomStringOfLettersOrDigits() {
        Assert.assertNotNull(RandomUtils.getRandomStringOfLettersOrDigits(5));
    }
}
