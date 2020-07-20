package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import testing.TestingUtils;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.facility.FacilityDataGenerator;
import com.imsweb.datagenerator.provider.facility.FacilityDto;
import com.imsweb.datagenerator.provider.physician.PhysicianDataGenerator;
import com.imsweb.datagenerator.provider.physician.PhysicianDto;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.fixed.FixedColumnsField;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;

public class NaaccrFixedColumnsDataGeneratorTest {

    // we are going to use this layout
    private static final NaaccrLayout _LAYOUT = LayoutFactory.getNaaccrFixedColumnsLayout(LayoutFactory.LAYOUT_ID_NAACCR_18_ABSTRACT);

    @Test
    public void testGenerator() {
        NaaccrFixedColumnsDataGenerator generator = new NaaccrFixedColumnsDataGenerator(_LAYOUT);

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

        Assert.assertNotNull(generator.getNumTumorsPerPatientDistribution());

        // test no rules
        generator.getPatientRules().clear();
        Assert.assertTrue(generator.getPatientRules().isEmpty());
        generator.getTumorRules().clear();
        Assert.assertTrue(generator.getTumorRules().isEmpty());

        // a layout is required
        try {
            //noinspection
            new NaaccrFixedColumnsDataGenerator((NaaccrLayout)null);
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
    public void testGeneratePatient() throws IOException {
        NaaccrFixedColumnsDataGenerator generator = new NaaccrFixedColumnsDataGenerator(_LAYOUT.getLayoutId());

        // null options, 1 tumor
        List<Map<String, String>> patient = generator.generatePatient(1, null);
        Assert.assertEquals(1, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNull(patient.get(0).get("addressAtDxState"));
        Assert.assertNull(_LAYOUT.validateLine(_LAYOUT.createLineFromRecord(patient.get(0), null), 1));

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
        Assert.assertEquals("HI", patient.get(0).get("addrAtDxState"));

        // test pre-processing constant value (since we are using a field that has a rule, it should be overridden)
        options.setConstantValuesPreProcessing(Collections.singletonMap("nameLast", "TEST"));
        patient = generator.generatePatient(1, options);
        Assert.assertNotEquals("TEST", patient.get(0).get("nameLast"));

        // test post-processing constant value (this value should NOT be overridden)
        options.setConstantValuesPostProcessing(Collections.singletonMap("nameLast", "TEST"));
        patient = generator.generatePatient(1, options);
        Assert.assertEquals("TEST", patient.get(0).get("nameLast"));

        // Test context
        int numTumors = 2;
        options = new NaaccrDataGeneratorOptions();
        options.setMinDxYear(2000);
        options.setMaxDxYear(2005);
        patient = generator.generatePatient(numTumors, options);

        LocalDate dateOfDx1 = LocalDate.of(Integer.parseInt(patient.get(0).get("dateOfDiagnosisYear")), Integer.parseInt(patient.get(0).get("dateOfDiagnosisMonth")),
                Integer.parseInt(patient.get(0).get("dateOfDiagnosisDay")));
        LocalDate dateOfDx2 = LocalDate.of(Integer.parseInt(patient.get(1).get("dateOfDiagnosisYear")), Integer.parseInt(patient.get(1).get("dateOfDiagnosisMonth")),
                Integer.parseInt(patient.get(1).get("dateOfDiagnosisDay")));

        boolean dateInRange1 = dateOfDx1.isAfter(options.getMinDxDate().minusDays(1)) && dateOfDx1.isBefore(options.getMaxDxDate().plusDays(1));
        boolean dateInRange2 = dateOfDx2.isAfter(options.getMinDxDate().minusDays(1)) && dateOfDx2.isBefore(options.getMaxDxDate().plusDays(1));

        Assert.assertTrue("Diagnosis Date outside options Minimum and Maximum.", dateInRange1 || dateInRange2);

        // another test with an incidence generator
        generator = new NaaccrFixedColumnsDataGenerator(LayoutFactory.LAYOUT_ID_NAACCR_18_INCIDENCE);
        patient = generator.generatePatient(1);
        Assert.assertEquals(1, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNull(patient.get(0).get("nameLast"));
    }

    @Test
    public void testGenerateFile() throws IOException {
        NaaccrFixedColumnsDataGenerator generator = new NaaccrFixedColumnsDataGenerator(_LAYOUT);

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

    @Test
    public void testGenerateWithProviders() {
        NaaccrFixedColumnsDataGenerator generator = new NaaccrFixedColumnsDataGenerator(_LAYOUT.getLayoutId());
        NaaccrDataGeneratorOptions genOptions = new NaaccrDataGeneratorOptions();
        genOptions.setState("CA");

        // Test Generation with no Facilities or Physicians
        List<Map<String, String>> patient = generator.generatePatient(1, null);

        Assert.assertEquals(1, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNull(patient.get(0).get("npiReportingFacility"));
        Assert.assertNull(patient.get(0).get("npiPhysicianManaging"));
        Assert.assertNull(patient.get(0).get("npiPhysicianFollowUp"));
        Assert.assertNull(patient.get(0).get("npiPhysicianPrimarySurg"));
        Assert.assertNull(patient.get(0).get("physicianManaging"));
        Assert.assertNull(patient.get(0).get("physicianFollowUp"));
        Assert.assertNull(patient.get(0).get("physicianPrimarySurg"));

        // Provider Options
        ProviderDataGeneratorOptions providerOptions = new ProviderDataGeneratorOptions();
        providerOptions.setState(genOptions.getState());

        // Create Facilities
        FacilityDataGenerator providerFacilityGenerator = new FacilityDataGenerator();
        List<FacilityDto> facilityList = providerFacilityGenerator.generateFacilities(20, providerOptions);

        // Create Physicians
        PhysicianDataGenerator providerPhysicianGenerator = new PhysicianDataGenerator();
        List<PhysicianDto> physicianList = providerPhysicianGenerator.generatePhysicians(20, providerOptions);

        genOptions.setFacilities(facilityList);
        genOptions.setPhysicians(physicianList);

        // Test Generation with Facilities and Physicians
        patient = generator.generatePatient(1, genOptions);
        Assert.assertEquals(1, patient.size());
        Assert.assertNotNull(patient.get(0).get("primarySite"));
        Assert.assertNotNull(patient.get(0).get("npiReportingFacility"));
        Assert.assertNotNull(patient.get(0).get("npiPhysicianManaging"));
        Assert.assertNotNull(patient.get(0).get("npiPhysicianFollowUp"));
        Assert.assertNotNull(patient.get(0).get("npiPhysicianPrimarySurg"));
        Assert.assertNotNull(patient.get(0).get("physicianManaging"));
        Assert.assertNotNull(patient.get(0).get("physicianFollowUp"));
        Assert.assertNotNull(patient.get(0).get("physicianPrimarySurg"));

    }

    /**
     * Testing patient rule. only assigns a single value to the nameLast field in the output file.
     */
    private static class TestPatientRule extends NaaccrDataGeneratorRule {

        private final String _value;

        public TestPatientRule(String value) {
            super("test-patient", "Testing patient rule");
            _value = value;
        }

        @Override
        public void execute(Map<String, String> patient, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
            patient.put("nameLast", _value);
        }
    }

    /**
     * Testing tumor rule. only assigns a single value to the primarySite field in the output file.
     */
    private static class TestTumorRule extends NaaccrDataGeneratorRule {

        private final String _value;

        public TestTumorRule(String value) {
            super("test-tumor", "Testing tumor rule");
            _value = value;
        }

        @Override
        public void execute(Map<String, String> tumor, List<Map<String, String>> otherTumors, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
            tumor.put("primarySite", _value);
        }
    }

    // *** TEMPORARY FOR TESTING ***
    // ABH 3/16/18
    /*
    @Test
    @SuppressWarnings("ConstantConditions")
    public void testDisplayPatient() {
        NaaccrDataGenerator generator = new NaaccrDataGenerator(_LAYOUT.getLayoutId());
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();

        //options.setMinDxYear(2003);
        //options.setMaxDxYear(2004);

        List<Map<String, String>> patient;

        final int NUM_PATIENTS = 1000;

        String header = "Patient Num \tpatientIdNumber \tnameFirst \tnameLast \tsex \tdateOfBirthYear \tdateOfBirthMonth \tdateOfBirthDay \t";
        header += "Tumor Num \tseerRecordNumber \ttumorRecordNumber \tprimarySite \thistologicTypeIcdO3 \tbehaviorCodeIcdO3 \tageAtDiagnosis \tdateOfDiagnosisYear \tdateOfDiagnosisMonth \tdateOfDiagnosisDay";

        System.out.println(header);
        String line;
        for (int i = 0; i < NUM_PATIENTS; i++) {
            int numTumors = RandomUtils.nextInt(2) + 1;

            patient = generator.generatePatient(numTumors, options);

            int tumorCount = 0;
            for (Map<String, String> m : patient) {
                tumorCount++;

                line = "";
                line += i + "\t";
                line += m.get("patientIdNumber") + "\t";
                line += m.get("nameFirst") + "\t";
                line += m.get("nameLast") + "\t";
                line += m.get("sex") + "\t";
                line += m.get("dateOfBirthYear") + "\t";
                line += m.get("dateOfBirthMonth") + "\t";
                line += m.get("dateOfBirthDay") + "\t";

                line += tumorCount + "\t";
                line += m.get("seerRecordNumber") + "\t";
                line += m.get("tumorRecordNumber") + "\t";
                line += m.get("primarySite") + "\t";
                line += m.get("histologicTypeIcdO3") + "\t";
                line += m.get("behaviorCodeIcdO3") + "\t";
                line += m.get("ageAtDiagnosis") + "\t";
                line += m.get("dateOfDiagnosisYear") + "\t";
                line += m.get("dateOfDiagnosisMonth") + "\t";
                line += m.get("dateOfDiagnosisDay") + "\t";
                System.out.println(line);
            }
        }

    }
    */

}