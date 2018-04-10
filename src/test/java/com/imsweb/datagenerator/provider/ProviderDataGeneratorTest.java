/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.facility.FacilityDataGenerator;
import com.imsweb.datagenerator.provider.physician.PhysicianDataGenerator;

public class ProviderDataGeneratorTest {

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGenerateProvider() {
        // Facility ---------------------------------------------------------------
        ProviderDataGenerator generator = new FacilityDataGenerator();
        ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();

        // null options
        List<Map<String, String>> providers = generator.generateProviders(1, options);
        Assert.assertEquals(1, providers.size());
        Assert.assertNotNull(providers.get(0).get("npi"));
        Assert.assertNull(providers.get(0).get("addressAtDxState"));

        // null options, several tumors
        providers = generator.generateProviders(3, options);
        Assert.assertEquals(3, providers.size());
        Assert.assertNotNull(providers.get(0).get("npi"));
        Assert.assertNotNull(providers.get(1).get("npi"));
        Assert.assertNotNull(providers.get(2).get("npi"));

        // test state option
        options.setState("HI");
        providers = generator.generateProviders(1, options);
        Assert.assertEquals("HI", providers.get(0).get("addressState"));

        // Physician ---------------------------------------------------------------
        generator = new PhysicianDataGenerator();
        options = new ProviderDataGeneratorOptions();

        // null options
        providers = generator.generateProviders(1, options);
        Assert.assertEquals(1, providers.size());
        Assert.assertNotNull(providers.get(0).get("npi"));
        Assert.assertNull(providers.get(0).get("addressAtDxState"));

        // null options, several tumors
        providers = generator.generateProviders(3, options);
        Assert.assertEquals(3, providers.size());
        Assert.assertNotNull(providers.get(0).get("npi"));
        Assert.assertNotNull(providers.get(1).get("npi"));
        Assert.assertNotNull(providers.get(2).get("npi"));

        // test state option
        options.setState("HI");
        providers = generator.generateProviders(1, options);
        Assert.assertEquals("HI", providers.get(0).get("addressState"));
    }

}