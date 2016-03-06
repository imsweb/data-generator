package com.imsweb.datagenerator.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class UniformRandomValueGeneratorTest {

    @Test
    public void testRandomValueGeneratorFromURL() {
        UniformRandomValueGenerator generator = new UniformRandomValueGenerator("random/test_values.csv.gz");

        // verify that a small sample of values produced by the generator are valid values from the given resource file
        List<String> validTestValues = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        for (int i = 0; i < 30; i++)
            Assert.assertTrue(validTestValues.contains(generator.getRandomValue()));
    }

    @Test
    public void testAddAndGetRandomValue() {
        UniformRandomValueGenerator generator = new UniformRandomValueGenerator();

        // add only value; get 5 random values and assert that all are equal to this one item
        generator.add("only value");
        for (int i = 0; i < 10; i++)
            Assert.assertEquals("only value", generator.getRandomValue());

        // add the same value - this should just update the frequency, and results should be same as previous test
        generator.add("only value");
        for (int i = 0; i < 5; i++)
            Assert.assertEquals("only value", generator.getRandomValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueFromEmptyGenerator() {
        new UniformRandomValueGenerator().getRandomValue();
    }
}
