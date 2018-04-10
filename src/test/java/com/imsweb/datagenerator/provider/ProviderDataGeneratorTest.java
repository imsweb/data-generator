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


    /*
    @Test
    public void testGenerator() {

        // Generate Facilities

        ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();
        options.setState("AK");
        FacilityDataGenerator genFacility = new FacilityDataGenerator();
        String thisSpecialty;
        List<Map<String, String>> facilitiesList = genFacility.generateProviders(10, options);

        for (Map<String, String> facility : facilitiesList) {
            System.out.println("Facility --------------------------------");
            System.out.println("NPI:               " + facility.get("npi"));
            System.out.println("Name:              " + facility.get("name"));
            System.out.println("addressFirstLine:  " + facility.get("addressFirstLine"));
            System.out.println("addressSecondLine: " + facility.get("addressSecondLine"));
            System.out.println("addressCity:       " + facility.get("addressCity"));
            System.out.println("addressState:      " + facility.get("addressState"));
            System.out.println("addressPostalCode: " + facility.get("addressPostalCode"));
            System.out.println("addressTelephone:  " + facility.get("addressTelephone"));

            for (int i=1; i <= 15; i++)
            {
                thisSpecialty = facility.get("specialty" + i);
                if (thisSpecialty != null) {
                    System.out.println("specialty" + i + ":        " + thisSpecialty);
                }
            }
        }

        // Generate Physicians
        options = new ProviderDataGeneratorOptions();
        options.setState("AK");
        PhysicianDataGenerator genPhysician = new PhysicianDataGenerator();

        List<Map<String, String>> physiciansList = genPhysician.generateProviders(10, options);

        for (Map<String, String> physician : physiciansList) {
            System.out.println("Physician --------------------------------");
            System.out.println("NPI:               " + physician.get("npi"));
            System.out.println("nameLast:          " + physician.get("nameLast"));
            System.out.println("nameFirst:         " + physician.get("nameFirst"));
            System.out.println("nameMiddle:        " + physician.get("nameMiddle"));
            System.out.println("namePrefix:        " + physician.get("namePrefix"));
            System.out.println("nameSuffix:        " + physician.get("nameSuffix"));
            System.out.println("credentials:       " + physician.get("credentials"));
            System.out.println("addressFirstLine:  " + physician.get("addressFirstLine"));
            System.out.println("addressSecondLine: " + physician.get("addressSecondLine"));
            System.out.println("addressCity:       " + physician.get("addressCity"));
            System.out.println("addressState:      " + physician.get("addressState"));
            System.out.println("addressPostalCode: " + physician.get("addressPostalCode"));
            System.out.println("addressTelephone:  " + physician.get("addressTelephone"));

            for (int i=1; i <= 15; i++)
            {
                thisSpecialty = physician.get("specialty" + i);
                if (thisSpecialty != null) {
                    System.out.println("specialty" + i + ":        " + thisSpecialty);
                }
            }
        }
    }
    */

}