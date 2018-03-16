/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.naaccr.rule.patient.AddressCurrentRule;
import com.imsweb.datagenerator.naaccr.rule.patient.BirthRule;
import com.imsweb.datagenerator.naaccr.rule.patient.ComputedEthnicityRule;
import com.imsweb.datagenerator.naaccr.rule.patient.DeathRule;
import com.imsweb.datagenerator.naaccr.rule.patient.HispanicOriginRule;
import com.imsweb.datagenerator.naaccr.rule.patient.IhsRule;
import com.imsweb.datagenerator.naaccr.rule.patient.NameRule;
import com.imsweb.datagenerator.naaccr.rule.patient.PatientIdRule;
import com.imsweb.datagenerator.naaccr.rule.patient.RaceRule;
import com.imsweb.datagenerator.naaccr.rule.patient.SexRule;
import com.imsweb.datagenerator.naaccr.rule.patient.SsnRule;
import com.imsweb.datagenerator.naaccr.rule.patient.VitalStatusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.AddressAtDxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.AgeRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.CensusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.CollaborativeStageRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfConclusiveDxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfDiagnosisRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfInitialRxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfLastContactRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DxConfirmationRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MaritalStatusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MultiTumorsRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.NapiiaRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.NhiaRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.PrimaryPayerRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.RegistryIdRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.ReportingSourceRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.RxSummaryRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerCodingSystemRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerRecordNumberRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerTypeOfFollowUpRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SequenceNumberCentralRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SiteRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.TumorMarkerRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.TumorRecordNumberRule;
import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;
import com.imsweb.layout.Layout;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;

import static com.imsweb.datagenerator.utils.DistributionUtils.getAgeGroup;

/**
 * A NAACCR data generator can be used to create fake NAACCR data files.
 * <br/><br/>
 * The main difference with the generic data generator is that this one understands the concept of a patient, which contains a list of tumors.
 * Since the number of tumors per patient is randomized, the number of tumors to create must be provided as a parameter (instead of the number of
 * patients). At the end, each tumor is written to the NAACCR data file as one line.
 * <br/><br/>
 * This generator comes with a set of pre-defined rules. Those can be removed or overridden, and new rules can be added.
 */
public class NaaccrDataGenerator implements DataGenerator {

    // NAACCR layout to use for this generator
    private NaaccrLayout _layout;

    // list of patient specific rules to be executed in order
    private List<NaaccrDataGeneratorRule> _patientRules;

    // list of tumor specific rules to be executed in order
    private List<NaaccrDataGeneratorRule> _tumorRules;

    /**
     * Constructor
     * @param layoutId NAACCR layout ID to use for this generator
     */
    public NaaccrDataGenerator(String layoutId) {
        this(LayoutFactory.getLayout(layoutId));
    }

    /**
     * Constructor
     * @param layout NAACCR layout to use for this generator
     */
    public NaaccrDataGenerator(Layout layout) {
        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR data generator!");
        if (!(layout instanceof NaaccrLayout))
            throw new RuntimeException("A NAACCR layout is required for creating a NAACCR data generator!");
        _layout = (NaaccrLayout)layout;
        _patientRules = new ArrayList<>();
        _tumorRules = new ArrayList<>();

        // default patient rules
        _patientRules.add(new PatientIdRule());
        _patientRules.add(new SexRule());
        _patientRules.add(new RaceRule());
        _patientRules.add(new HispanicOriginRule());
        _patientRules.add(new SsnRule());
        _patientRules.add(new NameRule());
        _patientRules.add(new VitalStatusRule());
        _patientRules.add(new DeathRule());
        _patientRules.add(new BirthRule());
        _patientRules.add(new AddressCurrentRule());
        _patientRules.add(new ComputedEthnicityRule());
        _patientRules.add(new IhsRule());

        // default tumor rules
        _tumorRules.add(new RegistryIdRule());
        _tumorRules.add(new TumorRecordNumberRule());
        _tumorRules.add(new SeerRecordNumberRule());
        _tumorRules.add(new SequenceNumberCentralRule());
        _tumorRules.add(new DateOfDiagnosisRule());
        _tumorRules.add(new SiteRule());
        _tumorRules.add(new AgeRule());
        _tumorRules.add(new DateOfInitialRxRule());
        _tumorRules.add(new DateOfLastContactRule());
        _tumorRules.add(new AddressAtDxRule());
        _tumorRules.add(new MaritalStatusRule());
        _tumorRules.add(new DxConfirmationRule());
        _tumorRules.add(new ReportingSourceRule());
        _tumorRules.add(new CensusRule());
        _tumorRules.add(new RxSummaryRule());
        _tumorRules.add(new SeerTypeOfFollowUpRule());
        _tumorRules.add(new PrimaryPayerRule());
        _tumorRules.add(new TumorMarkerRule());
        _tumorRules.add(new SeerCodingSystemRule());
        _tumorRules.add(new MultiTumorsRule());
        _tumorRules.add(new DateOfConclusiveDxRule());
        _tumorRules.add(new CollaborativeStageRule());
        _tumorRules.add(new NhiaRule());
        _tumorRules.add(new NapiiaRule());
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    /**
     * Adds a new patient rule
     * @param rule rule to be added
     */
    public void addPatientRule(NaaccrDataGeneratorRule rule) {
        _patientRules.add(rule);
    }

    /**
     * Removes the requested patient rule.
     * @param ruleId rule ID to remove
     * @return true if the rule was actually removed
     */
    public boolean removePatientRule(String ruleId) {
        return _patientRules.remove(getPatientRule(ruleId));
    }

    /**
     * Replaced an existing rule by the provided rule, both rules must have the same ID.
     * @param newRule new rule to replace with
     * @return true if the rule was actually replaced
     */
    public boolean replacePatientRule(NaaccrDataGeneratorRule newRule) {
        int idx = _patientRules.indexOf(newRule);
        if (idx != -1) {
            _patientRules.set(idx, newRule);
            return true;
        }
        return false;
    }

    /**
     * Returns the requested patient rule from this generator.
     */
    public NaaccrDataGeneratorRule getPatientRule(String ruleId) {
        for (NaaccrDataGeneratorRule rule : _patientRules)
            if (ruleId.equals(rule.getId()))
                return rule;
        return null;
    }

    /**
     * Returns all the patient rules from this generator.
     */
    public List<NaaccrDataGeneratorRule> getPatientRules() {
        return _patientRules;
    }

    /**
     * Adds a new patient rule
     * @param rule rule to be added
     */
    public void addTumorRule(NaaccrDataGeneratorRule rule) {
        _tumorRules.add(rule);
    }

    /**
     * Removes the requested tumor rule.
     * @param ruleId rule ID to remove
     * @return true if the rule was actually removed
     */
    public boolean removeTumorRule(String ruleId) {
        return _tumorRules.remove(getTumorRule(ruleId));
    }

    /**
     * Replaced an existing rule by the provided rule, both rules must have the same ID.
     * @param newRule new rule to replace with
     * @return true if the rule was actually replaced
     */
    public boolean replaceTumorRule(NaaccrDataGeneratorRule newRule) {
        int idx = _tumorRules.indexOf(newRule);
        if (idx != -1) {
            _tumorRules.set(idx, newRule);
            return true;
        }
        return false;
    }

    /**
     * Returns the requested patient rule from this generator.
     */
    public NaaccrDataGeneratorRule getTumorRule(String ruleId) {
        for (NaaccrDataGeneratorRule rule : _tumorRules)
            if (ruleId.equals(rule.getId()))
                return rule;
        return null;
    }

    /**
     * Returns all the tumor rules from this generator.
     */
    public List<NaaccrDataGeneratorRule> getTumorRules() {
        return _tumorRules;
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param numTumors number of tumors to generate
     * @return generated patient as a list of tumor maps, never null
     */
    public List<Map<String, String>> generatePatient(int numTumors) {
        return generatePatient(numTumors, null);
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param numTumors number of tumors to generate
     * @param options options
     * @return generated patient as a list of tumor maps, never null
     */
    public List<Map<String, String>> generatePatient(int numTumors, NaaccrDataGeneratorOptions options) {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // Context contains information that is shared amongst the rules. Contains values we want to use for this patient.
        Map<String, String> context = generateInitialPatientContext(numTumors);

        List<Map<String, String>> tumors = new ArrayList<>();

        // execute the patient rules once; we will copy the resulting values in each tumor
        Map<String, String> patient = new HashMap<>();
        for (NaaccrDataGeneratorRule rule : _patientRules)
            if (allPropertiesHaveValue(patient, rule.getRequiredProperties()))
                rule.execute(patient, null, options, context);

        // create each tumor and add it to the return list
        for (int i = 0; i < numTumors; i++) {
            if (context != null)
                if (context.get("totalTumorCount") != null)
                    context.put("currentTumor", String.valueOf(i));

            Map<String, String> tumor = new HashMap<>();

            // if there is pre-processing constant values, set them
            if (options.getConstantValuesPreProcessing() != null)
                tumor.putAll(options.getConstantValuesPreProcessing());

            // copy the patient values
            tumor.putAll(patient);

            // execute the tumor rules
            for (NaaccrDataGeneratorRule rule : _tumorRules)
                if (allPropertiesHaveValue(tumor, rule.getRequiredProperties()))
                    rule.execute(tumor, tumors, options, context);

            // if there is post-processing constant values, set them
            if (options.getConstantValuesPostProcessing() != null)
                tumor.putAll(options.getConstantValuesPostProcessing());

            // we are done with this tumor, add it to the result
            tumors.add(tumor);
        }

        return tumors;
    }

    /**
     * Generates a list of values that we want this patient to have.
     * Specifically the sex of the patient and the sites for the tumors.
     * <br/><br/>
     * @param numTumors number of tumors to generate, must be greater than 0
     */
    private Map<String, String> generateInitialPatientContext(int numTumors) {

        // Context contains information that is shared amongst the rules. Contains values we want to use for this patient.
        Map<String, String> context = new HashMap<>();

        // Pick the patient sex. The sex of the patient will influence the tumor sites.
        context.put("sex", DistributionUtils.getSex());

        // Pick the sites we want for the tumors. The Tumor sites can influence the patient's age.
        context.put("currentTumor", "");
        context.put("totalTumorCount", String.valueOf(numTumors));
        for (int i = 0; i < numTumors; i++) {
            SiteFrequencyDto dto = DistributionUtils.getSite(context.get("sex"));
            String tumorName = "tumor" + i;
            context.put(tumorName + " primarySite", dto.getSite());
            context.put(tumorName + " histologyIcdO3", dto.getHistology());
            context.put(tumorName + " behaviorIcdO3", dto.getBehavior());
            int tumorAgeGroup = getAgeGroup(dto.getSite());
            context.put(tumorName + " ageGroup", String.valueOf(tumorAgeGroup));
        }

        return context;
    }





    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numTumors number of tumors to generate, must be greater than 0
     */
    public void generateFile(File file, int numTumors) throws IOException {
        generateFile(file, numTumors, null);
    }

    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numTumors number of tumors to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numTumors, NaaccrDataGeneratorOptions options) throws IOException {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // create a random distribution for the number of tumors, if we have to
        Distribution<Integer> numTumGen = options.getNumTumorsPerPatient() == null ? getNumTumorsPerPatientDistribution() : null;

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            int numCreatedTumors = 0;
            while (numCreatedTumors < numTumors) {
                int numTumorForThisPatient = numTumGen == null ? options.getNumTumorsPerPatient() : numTumGen.getValue();
                // never create more tumors than requested, so we use a min() call
                for (Map<String, String> tumor : generatePatient(Math.min(numTumorForThisPatient, numTumors - numCreatedTumors), options)) {
                    _layout.writeRecord(writer, tumor);
                    numCreatedTumors++;
                }
            }
        }
    }

    /**
     * Returns the distribution to use for generating the number of tumors for a specific patient.
     */
    public Distribution<Integer> getNumTumorsPerPatientDistribution() {
        Map<Integer, Double> frequencies = new HashMap<>();
        frequencies.put(1, 95.0);
        frequencies.put(2, 4.0);
        frequencies.put(3, 1.0);
        return Distribution.of(frequencies);
    }

    /**
     * Returns true if all the requested properties have a non-blank value on the provided record.
     */
    protected boolean allPropertiesHaveValue(Map<String, String> record, List<String> properties) {
        for (String property : properties) {
            String val = record.get(property);
            if (val == null || val.trim().isEmpty())
                return false;
        }
        return true;
    }
}
