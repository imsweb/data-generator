package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import testing.TestingUtils;

import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.fixed.FixedColumnsField;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;

public class NaaccrDataGeneratorTest {

    // we are going to use this layout
    private static final NaaccrLayout _LAYOUT = (NaaccrLayout)LayoutFactory.getLayout(LayoutFactory.LAYOUT_ID_NAACCR_16_ABSTRACT);

    @Test
    public void testGenerator() {
        NaaccrDataGenerator generator = new NaaccrDataGenerator(_LAYOUT);

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
            new NaaccrDataGenerator(null);
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
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGeneratePatient() throws IOException {
        NaaccrDataGenerator generator = new NaaccrDataGenerator(_LAYOUT);

        // null options, 1 tumor
        List<Map<String, String>> patient = generator.generatePatient(1, null);
        Assert.assertEquals(1, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNull(patient.get(0).get("addressAtDxState"));
        Assert.assertNull(_LAYOUT.validateLine(_LAYOUT.createLineFromRecord(patient.get(0)), 1));

        // null options, several tumors
        patient = generator.generatePatient(3, null);
        Assert.assertEquals(3, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNotNull(patient.get(1).get("primarySite"));
        Assert.assertNotNull(patient.get(2).get("primarySite"));

        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();

        // test state option
        options.setState("HI");
        patient = generator.generatePatient(1, options);
        Assert.assertEquals("HI", patient.get(0).get("addressAtDxState"));

        // test pre-processing constant value (since we are using a field that has a rule, it should be overridden)
        options.setConstantValuesPreProcessing(Collections.singletonMap("nameLast", "TEST"));
        patient = generator.generatePatient(1, options);
        Assert.assertNotEquals("TEST", patient.get(0).get("nameLast"));

        // test post-processing constant value (this value should NOT be overridden)
        options.setConstantValuesPostProcessing(Collections.singletonMap("nameLast", "TEST"));
        patient = generator.generatePatient(1, options);
        Assert.assertEquals("TEST", patient.get(0).get("nameLast"));
    }

    @Test
    public void testGenerateFile() throws IOException {
        NaaccrDataGenerator generator = new NaaccrDataGenerator(_LAYOUT);

        // regular file, 1 record
        File file = TestingUtils.createFile("naaccr-data-generator-1-rec.txt");
        generator.generateFile(file, 1, null);
        List<String> lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // regular file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 1 record
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt.gz");
        generator.generateFile(file, 1, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt.gz");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // use an option for the number of tumors per patient (3 per; 99 total, so there should be 33 patients)
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setNumTumorsPerPatient(3);
        generator.generateFile(file, 99, options);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(99, lines.size());
        FixedColumnsField patientIdNumberField = _LAYOUT.getFieldByName("patientIdNumber");
        Assert.assertNotNull(patientIdNumberField);
        Set<String> patientIdNumberValues = new HashSet<>();
        for (String line : lines)
            patientIdNumberValues.add(line.substring(patientIdNumberField.getStart() - 1, patientIdNumberField.getEnd()));
        Assert.assertEquals(33, patientIdNumberValues.size());

        // bad number of tumors
        try {
            generator.generateFile(file, 0, null);
            Assert.fail();
        }
        catch (RuntimeException e) {
            // expected
        }
    }

    /**
     * Testing patient rule. only assigns a single value to the nameLast field in the output file.
     */
    private class TestPatientRule extends NaaccrDataGeneratorRule {

        private String _value;

        public TestPatientRule(String value) {
            super("test-patient", "Testing patient rule");
            _value = value;
        }

        @Override
        public void execute(Map<String, String> patient, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options) {
            patient.put("nameLast", _value);
        }
    }

    /**
     * Testing tumor rule. only assigns a single value to the primarySite field in the output file.
     */
    private class TestTumorRule extends NaaccrDataGeneratorRule {

        private String _value;

        public TestTumorRule(String value) {
            super("test-tumor", "Testing tumor rule");
            _value = value;
        }

        @Override
        public void execute(Map<String, String> tumor, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options) {
            tumor.put("primarySite", _value);
        }
    }
}