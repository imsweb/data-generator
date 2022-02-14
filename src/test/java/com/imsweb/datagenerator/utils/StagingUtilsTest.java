/*
 * Copyright (C) 2022 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import org.junit.Assert;
import org.junit.Test;

public class StagingUtilsTest {

    @Test
    public void testCreateDistribution() {
        // no ranges, all element should have a frequency of 1
        for (DistributionElement<String> element : StagingUtils.createDistribution(new String[] {"1", "2", "3"}).getFrequencies())
            Assert.assertEquals(1, element.getFrequency().intValue());

        // range contains 8 values, it should have a frequency of 8, other elements should have a frequency of 1
        for (DistributionElement<String> element : StagingUtils.createDistribution(new String[] {"1", "2-9", "10"}).getFrequencies()) {
            if (element.getValue().equals("2-9"))
                Assert.assertEquals(8, element.getFrequency().intValue());
            else
                Assert.assertEquals(1, element.getFrequency().intValue());
        }
    }

    @Test
    public void testReformatValueWithPeriod() {
        Assert.assertEquals("0.0", StagingUtils.reformatValueWithPeriod("00"));
        Assert.assertEquals("0.1", StagingUtils.reformatValueWithPeriod("01"));
        Assert.assertEquals("1.0", StagingUtils.reformatValueWithPeriod("10"));
        Assert.assertEquals("99.9", StagingUtils.reformatValueWithPeriod("999"));
    }
}
