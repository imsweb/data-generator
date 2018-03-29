/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class FacilityDataGeneratorTest {

    @Test
    public void testGenerator() {

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

    /**
     * Testing patient rule. only assigns a single value to the nameLast field in the output file.
     */
    private class TestNameRule extends ProviderDataGeneratorRule {

        private String _value;

        public TestNameRule(String value) {
            super("test-patient", "Testing patient rule");
            _value = value;
        }

        @Override
        public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {
            provider.put("nameLast", _value);
        }
    }

}