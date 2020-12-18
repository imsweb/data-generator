/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.imsweb.datagenerator.naaccr.rule.tumor.FacilityRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MaritalStatusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MultiTumorsRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.NapiiaRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.NhiaRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.PhysicianRule;
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

/**
 * A NAACCR data generator can be used to create fake NAACCR data files.
 * <br/><br/>
 * The main difference with the generic data generator is that this one understands the concept of a patient, which contains a list of tumors.
 * Since the number of tumors per patient is randomized, the number of tumors to create must be provided as a parameter (instead of the number of
 * patients). At the end, each tumor is written to the NAACCR data file as one line.
 * <br/><br/>
 * This generator comes with a set of pre-defined rules. Those can be removed or overridden, and new rules can be added.
 */
public abstract class NaaccrDataGenerator implements DataGenerator {

    public static final String CONTEXT_FLAG_SEX = "sex";
    public static final String CONTEXT_FLAG_CURRENT_TUMOR_INDEX = "currentTumorIndex";
    public static final String CONTEXT_FLAG_SITE_FREQ_MAP = "siteFreqMap";
    public static final String CONTEXT_FLAG_AGE_GROUP_MAP = "ageGroupMap";
    public static final String CONTEXT_FLAG_MAX_AGE_GROUP = "maxAgeGroup";

    // list of patient specific rules to be executed in order
    private final List<NaaccrDataGeneratorRule> _patientRules;

    // list of tumor specific rules to be executed in order
    private final List<NaaccrDataGeneratorRule> _tumorRules;

    /**
     * Constructor
     * @param useMaidenNameField if true, the name rule will use the 'nameMaiden' field, otherwise it will use the 'nameBirthSurname' field
     */
    public NaaccrDataGenerator(boolean useMaidenNameField) {

        // default patient rules
        _patientRules = new ArrayList<>();
        _patientRules.add(new PatientIdRule());
        _patientRules.add(new SexRule());
        _patientRules.add(new RaceRule());
        _patientRules.add(new HispanicOriginRule());
        _patientRules.add(new SsnRule());
        _patientRules.add(new NameRule(useMaidenNameField));
        _patientRules.add(new VitalStatusRule());
        _patientRules.add(new DeathRule());
        _patientRules.add(new BirthRule());
        _patientRules.add(new AddressCurrentRule());
        _patientRules.add(new ComputedEthnicityRule());
        _patientRules.add(new IhsRule());

        // default tumor rules
        _tumorRules = new ArrayList<>();
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
        _tumorRules.add(new FacilityRule());
        _tumorRules.add(new PhysicianRule());
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

    // helper, creates a single patient as a list of maps
    protected List<Map<String, String>> generatePatientAsListOfMaps(int numTumors, NaaccrDataGeneratorOptions options) {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // Context contains information that is shared amongst the rules. Contains values we want to use for this patient.
        Map<String, Object> context = generateInitialPatientContext(numTumors);

        List<Map<String, String>> tumors = new ArrayList<>();

        // execute the patient rules once; we will copy the resulting values in each tumor
        Map<String, String> patient = new HashMap<>();
        for (NaaccrDataGeneratorRule rule : _patientRules)
            if (allPropertiesHaveValue(patient, rule.getRequiredProperties()))
                rule.execute(patient, null, options, context);

        // filter created keys to make sure they are in the layout
        Set<String> invalidKeys = patient.keySet().stream().filter(this::isInvalidValidField).collect(Collectors.toSet());
        invalidKeys.forEach(patient::remove);

        // create each tumor and add it to the return list
        for (int i = 0; i < numTumors; i++) {
            context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, i);

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

            // filter created keys to make sure they are in the layout
            invalidKeys = tumor.keySet().stream().filter(this::isInvalidValidField).collect(Collectors.toSet());
            invalidKeys.forEach(tumor::remove);

            // if there is post-processing constant values, set them
            if (options.getConstantValuesPostProcessing() != null)
                tumor.putAll(options.getConstantValuesPostProcessing());

            // we are done with this tumor, add it to the result
            tumors.add(tumor);
        }

        return tumors;
    }

    protected abstract boolean isInvalidValidField(String name);

    /**
     * Generates a list of values that we want this patient to have.
     * Specifically the sex of the patient and the sites for the tumors.
     * <br/><br/>
     * @param numTumors number of tumors to generate, must be greater than 0
     */
    private Map<String, Object> generateInitialPatientContext(int numTumors) {

        // Context contains information that is shared amongst the rules. Contains values we want to use for this patient.
        Map<String, Object> context = new HashMap<>();

        // Pick the patient sex. The sex of the patient will influence the tumor sites.
        context.put(CONTEXT_FLAG_SEX, DistributionUtils.getSex());

        // Pick the sites we want for the tumors. The Tumor sites can influence the patient's age.
        context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, -1);

        Map<Integer, SiteFrequencyDto> siteFreqMap = new HashMap<>();
        Map<Integer, Integer> ageGroupMap = new HashMap<>();
        int maxAgeGroup = -1;
        for (int i = 0; i < numTumors; i++) {
            SiteFrequencyDto dto = DistributionUtils.getSite((String)context.get(CONTEXT_FLAG_SEX));
            siteFreqMap.put(i, dto);
            int iAgeGroup = DistributionUtils.getAgeGroup(dto.getSite());
            ageGroupMap.put(i, iAgeGroup);
            maxAgeGroup = Integer.max(maxAgeGroup, iAgeGroup);
        }
        context.put(CONTEXT_FLAG_SITE_FREQ_MAP, siteFreqMap);
        context.put(CONTEXT_FLAG_AGE_GROUP_MAP, ageGroupMap);
        context.put(CONTEXT_FLAG_MAX_AGE_GROUP, maxAgeGroup);

        return context;
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
