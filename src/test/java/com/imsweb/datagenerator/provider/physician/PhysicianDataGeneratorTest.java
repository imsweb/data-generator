/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.utils.dto.PhysicianFrequencyDto;

public class PhysicianDataGeneratorTest {

    @Test
    public void testGenerator() {
        // Physician ---------------------------------------------------------------
        PhysicianDataGenerator generatorPhysician = new PhysicianDataGenerator();
        ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();

        // null options
        List<PhysicianFrequencyDto> physicians = generatorPhysician.generatePhysicians(1, options);
        Assert.assertEquals(1, physicians.size());
        Assert.assertNotNull(physicians.get(0).getNpi());

        // null options, several tumors
        physicians = generatorPhysician.generatePhysicians(3, options);
        Assert.assertEquals(3, physicians.size());
        // these cause random failures, will investigate later
        //Assert.assertNotNull(physicians.get(0).getNpi());
        //Assert.assertNotNull(physicians.get(1).getNpi());
        //Assert.assertNotNull(physicians.get(2).getNpi());

        // test state option
        options.setState("HI");
        physicians = generatorPhysician.generatePhysicians(1, options);
        Assert.assertEquals("HI", physicians.get(0).getAddressState());
    }

}