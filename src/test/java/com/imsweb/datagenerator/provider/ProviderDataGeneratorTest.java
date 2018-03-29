/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.imsweb.datagenerator.provider.facility.FacilityDataGenerator;
import com.imsweb.datagenerator.provider.physician.PhysicianDataGenerator;

public class ProviderDataGeneratorTest {

    @Test
    public void testGenerator() {

        // Generate Facilities

        ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();
        options.setState("AK");
        FacilityDataGenerator genFacility = new FacilityDataGenerator();

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
            System.out.println("specialty1:        " + facility.get("specialty1"));
            System.out.println("specialty2:        " + facility.get("specialty2"));
            System.out.println("specialty3:        " + facility.get("specialty3"));
            System.out.println("specialty4:        " + facility.get("specialty4"));
            System.out.println("specialty5:        " + facility.get("specialty5"));
            System.out.println("specialty6:        " + facility.get("specialty6"));
            System.out.println("specialty7:        " + facility.get("specialty7"));
            System.out.println("specialty8:        " + facility.get("specialty8"));
            System.out.println("specialty9:        " + facility.get("specialty9"));
            System.out.println("specialty10:       " + facility.get("specialty10"));
            System.out.println("specialty11:       " + facility.get("specialty11"));
            System.out.println("specialty12:       " + facility.get("specialty12"));
            System.out.println("specialty13:       " + facility.get("specialty13"));
            System.out.println("specialty14:       " + facility.get("specialty14"));
            System.out.println("specialty15:       " + facility.get("specialty15"));
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
            System.out.println("specialty1:        " + physician.get("specialty1"));
            System.out.println("specialty2:        " + physician.get("specialty2"));
            System.out.println("specialty3:        " + physician.get("specialty3"));
            System.out.println("specialty4:        " + physician.get("specialty4"));
            System.out.println("specialty5:        " + physician.get("specialty5"));
            System.out.println("specialty6:        " + physician.get("specialty6"));
            System.out.println("specialty7:        " + physician.get("specialty7"));
            System.out.println("specialty8:        " + physician.get("specialty8"));
            System.out.println("specialty9:        " + physician.get("specialty9"));
            System.out.println("specialty10:       " + physician.get("specialty10"));
            System.out.println("specialty11:       " + physician.get("specialty11"));
            System.out.println("specialty12:       " + physician.get("specialty12"));
            System.out.println("specialty13:       " + physician.get("specialty13"));
            System.out.println("specialty14:       " + physician.get("specialty14"));
            System.out.println("specialty15:       " + physician.get("specialty15"));
        }



        /*
        // default generator comes with some patient and tumor rules
        Assert.assertFalse(generator.getPatientRules().isEmpty());
        for (NaaccrDataGeneratorRule rule : generator.getPatientRules()) {
            Assert.assertNotNull(rule.getId());
            Assert.assertNotNull(rule.getName());
        }
        Assert.assertFalse(generator.getTumorRules().isEmpty());
        for (NaaccrDataGeneratorRule rule : generator.getTumorRules()) {
            Assert.assertNotNull(rule.getId());
            Assert.assertNotNull(rule.getName());
        }

        // test no rules
        generator.getPatientRules().clear();
        Assert.assertTrue(generator.getPatientRules().isEmpty());
        generator.getTumorRules().clear();
        Assert.assertTrue(generator.getTumorRules().isEmpty());

        // a layout is required
        try {
            //noinspection ConstantConditions
            new NaaccrDataGenerator((Layout)null);
            Assert.fail();
        }
        catch (RuntimeException e) {
            // expected
        }

        // ********** test patient operations

        // add a patient rule
        generator.addPatientRule(new TestPatientRule("val1"));
        Assert.assertNotNull(generator.getPatientRule("test-patient"));

        // execute the rule
        Assert.assertEquals("val1", generator.generatePatient(1, null).get(0).get("nameLast"));

        // replace a patient rule
        Assert.assertTrue(generator.replacePatientRule(new TestPatientRule("val2")));
        Assert.assertEquals("val2", generator.generatePatient(1, null).get(0).get("nameLast"));

        // remove a patient rule
        Assert.assertTrue(generator.removePatientRule("test-patient"));
        Assert.assertNull(generator.getPatientRule("test-patient"));

        // get unknown patient rule
        Assert.assertNull(generator.getPatientRule("hum?"));

        // remove unknown patient rule
        Assert.assertFalse(generator.removePatientRule("hum?"));

        // replace unknown patient rule
        Assert.assertFalse(generator.replacePatientRule(new TestPatientRule(null)));

        // ********** test tumor operations

        // add a tumor rule
        generator.addTumorRule(new TestTumorRule("C123"));
        Assert.assertNotNull(generator.getTumorRule("test-tumor"));

        // execute the rule
        Assert.assertEquals("C123", generator.generatePatient(1, null).get(0).get("primarySite"));

        // replace a tumor rule
        Assert.assertTrue(generator.replaceTumorRule(new TestTumorRule("C456")));
        Assert.assertEquals("C456", generator.generatePatient(1, null).get(0).get("primarySite"));

        // remove a tumor rule
        Assert.assertTrue(generator.removeTumorRule("test-tumor"));
        Assert.assertNull(generator.getTumorRule("test-tumor"));

        // get unknown tumor rule
        Assert.assertNull(generator.getTumorRule("hum?"));

        // remove unknown tumor rule
        Assert.assertFalse(generator.removeTumorRule("hum?"));

        // replace unknown tumor rule
        Assert.assertFalse(generator.replacePatientRule(new TestTumorRule(null)));
        */
    }

}