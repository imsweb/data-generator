/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician;


import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGenerator;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class PhysicianDataGeneratorTest {

    @Test
    public void testGenerator() {
        ProviderDataGenerator generator = new PhysicianDataGenerator();

        // default generator comes with some patient and tumor rules
        Assert.assertFalse(generator.getRules().isEmpty());
        for (ProviderDataGeneratorRule rule : generator.getRules()) {
            Assert.assertNotNull(rule.getId());
            Assert.assertNotNull(rule.getName());
        }

        // test no rules
        generator.getRules().clear();
        Assert.assertTrue(generator.getRules().isEmpty());
    }

}