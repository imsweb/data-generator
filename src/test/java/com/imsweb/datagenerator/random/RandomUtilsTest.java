package com.imsweb.datagenerator.random;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void testNextInt() {
        int i = RandomUtils.nextInt(10);
        Assert.assertTrue(i >= 0 && i < 10);

        i = RandomUtils.nextInt(100);
        Assert.assertTrue(i > 0 && i < 100);
    }

    @Test
    public void testNextDouble() {
        double d = RandomUtils.nextDouble();
        Assert.assertTrue(d >= 0.0 && d <= 1.0);
    }
}
