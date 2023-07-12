/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.utils.dto.FacilityDto;

public class FacilityDataGeneratorTest {

    @Test
    public void testGenerator() {
        // Facility ---------------------------------------------------------------
        FacilityDataGenerator generatorFacility = new FacilityDataGenerator();
        ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();

        // null options
        List<FacilityDto> facilities = generatorFacility.generateFacilities(1, options);
        Assert.assertEquals(1, facilities.size());
        Assert.assertNotNull(facilities.get(0).getNpi());

        // null options, several tumors
        facilities = generatorFacility.generateFacilities(3, options);
        Assert.assertEquals(3, facilities.size());
        Assert.assertNotNull(facilities.get(0).getNpi());
        Assert.assertNotNull(facilities.get(1).getNpi());
        Assert.assertNotNull(facilities.get(2).getNpi());

        // test state option
        options.setState("HI");
        facilities = generatorFacility.generateFacilities(1, options);
        Assert.assertEquals("HI", facilities.get(0).getAddressState());
    }

}