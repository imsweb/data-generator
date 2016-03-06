package com.imsweb.datagenerator.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DistributedRandomValueGeneratorTest {

    @Test
    public void testRandomValueGeneratorFromURL() {
        DistributedRandomValueGenerator generator = new DistributedRandomValueGenerator("random/test_frequencies.csv.gz");

        // verify that a small sample of values produced by the generator are valid values from the given resource file
        List<String> expected = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        for (int i = 0; i < 20; i++)
            Assert.assertTrue(expected.contains(generator.getRandomValue()));
    }

    @Test
    public void testAddAndGetRandomValue() {
        // create an empty generator
        DistributedRandomValueGenerator generator = new DistributedRandomValueGenerator();

        // add only value; get 5 random values and assert that all are equal to this one item
        generator.add("only value", 1);
        for (int i = 0; i < 5; i++)
            Assert.assertEquals("only value", generator.getRandomValue());

        // add the same value - this should just update the frequency, and results should be same as previous test
        generator.add("only value", 10);
        for (int i = 0; i < 5; i++)
            Assert.assertEquals("only value", generator.getRandomValue());
    }

    @Test
    public void testAddByListAndGetRandomValue() {
        DistributedRandomValueGenerator generator = new DistributedRandomValueGenerator();

        // add a list and pull a random value - it should be one of the values in the list
        List<String> expected = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        generator.add(expected, 1);
        for (int i = 0; i < 5; i++)
            Assert.assertTrue(expected.contains(generator.getRandomValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueFromEmptyGenerator() {
        new DistributedRandomValueGenerator().getRandomValue();
    }
}
