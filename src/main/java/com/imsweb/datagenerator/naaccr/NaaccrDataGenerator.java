/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.naaccr.rule.patient.AddressCurrentRule;
import com.imsweb.datagenerator.naaccr.rule.patient.BirthRule;
import com.imsweb.datagenerator.naaccr.rule.patient.ComputedEthnicityRule;
import com.imsweb.datagenerator.naaccr.rule.patient.DeathRule;
import com.imsweb.datagenerator.naaccr.rule.patient.HispanicOriginRule;
import com.imsweb.datagenerator.naaccr.rule.patient.NameRule;
import com.imsweb.datagenerator.naaccr.rule.patient.PatientIdRule;
import com.imsweb.datagenerator.naaccr.rule.patient.RaceRule;
import com.imsweb.datagenerator.naaccr.rule.patient.SexRule;
import com.imsweb.datagenerator.naaccr.rule.patient.SsnRule;
import com.imsweb.datagenerator.naaccr.rule.patient.VitalStatusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.AddressAtDxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.AgeRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.CensusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfConclusiveDxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfDiagnosisRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfInitialRxRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DateOfLastContactRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DxConfirmationRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.DxProcTextRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.FacilityRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MaritalStatusRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.MultiTumorsRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.PhysicianRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.PrimaryPayerRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.ReportingSourceRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.RxSummaryRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.RxTextRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerCodingSystemRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerRecordNumberRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SeerTypeOfFollowUpRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SequenceNumberCentralRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.SiteRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.StagingInputRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.TumorMarkerRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.TumorRecordNumberRule;
import com.imsweb.datagenerator.naaccr.rule.tumor.TumorTextRule;
import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;
import com.imsweb.naaccrxml.entity.AbstractEntity;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

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

    // list of rules (order matters)
    private final List<NaaccrDataGeneratorPatientRule> _patientRules;
    private final List<NaaccrDataGeneratorTumorRule> _tumorRules;

    /**
     * Constructor
     * @param useMaidenNameField if true, the name rule will use the 'nameMaiden' field, otherwise it will use the 'nameBirthSurname' field
     */
    protected NaaccrDataGenerator(boolean useMaidenNameField) {

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

        // default tumor rules
        _tumorRules = new ArrayList<>();
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
        _tumorRules.add(new StagingInputRule());
        _tumorRules.add(new FacilityRule());
        _tumorRules.add(new PhysicianRule());
        _tumorRules.add(new TumorTextRule());
        _tumorRules.add(new DxProcTextRule());
        _tumorRules.add(new RxTextRule());
    }

    /**
     * Adds a new patient rule
     * @param rule rule to be added
     */
    public void addPatientRule(NaaccrDataGeneratorPatientRule rule) {
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
    public boolean replacePatientRule(NaaccrDataGeneratorPatientRule newRule) {
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
    public NaaccrDataGeneratorPatientRule getPatientRule(String ruleId) {
        for (NaaccrDataGeneratorPatientRule rule : _patientRules)
            if (ruleId.equals(rule.getId()))
                return rule;
        return null;
    }

    /**
     * Returns all the patient rules from this generator.
     */
    public List<NaaccrDataGeneratorPatientRule> getPatientRules() {
        return _patientRules;
    }

    /**
     * Adds a new patient rule
     * @param rule rule to be added
     */
    public void addTumorRule(NaaccrDataGeneratorTumorRule rule) {
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
    public boolean replaceTumorRule(NaaccrDataGeneratorTumorRule newRule) {
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
    public NaaccrDataGeneratorTumorRule getTumorRule(String ruleId) {
        for (NaaccrDataGeneratorTumorRule rule : _tumorRules)
            if (ruleId.equals(rule.getId()))
                return rule;
        return null;
    }

    /**
     * Returns all the tumor rules from this generator.
     */
    public List<NaaccrDataGeneratorTumorRule> getTumorRules() {
        return _tumorRules;
    }

    // helper, creates a single patient
    protected Patient internalGeneratePatient(int numTumors, NaaccrDataGeneratorOptions options, boolean useFullDates) {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // Context contains information that is shared amongst the rules. Contains values we want to use for this patient.
        Map<String, Object> context = generateInitialPatientContext(numTumors);

        // execute the patient rules once; we will copy the resulting values in each tumor
        Patient patient = new Patient();

        // if there is pre-processing constant values, set them
        if (options.getConstantPatientValuesPreProcessing() != null)
            for (Entry<String, String> entry : options.getConstantPatientValuesPreProcessing().entrySet())
                setValue(patient, entry.getKey(), entry.getValue());

        for (NaaccrDataGeneratorPatientRule rule : _patientRules)
            if (allPropertiesHaveValue(patient, null, rule.getRequiredProperties()))
                rule.execute(patient, options, context);

        // if there is post-processing constant values, set them
        if (options.getConstantPatientValuesPostProcessing() != null)
            for (Entry<String, String> entry : options.getConstantPatientValuesPostProcessing().entrySet())
                setValue(patient, entry.getKey(), entry.getValue());

        // filter created keys to make sure they are in the layout
        for (Item item : patient.getItems().stream().filter(item -> isInvalidValidField(item.getNaaccrId())).collect(Collectors.toList()))
            patient.removeItem(item);

        // create each tumor and add it to the return list
        for (int i = 0; i < numTumors; i++) {
            context.put(CONTEXT_FLAG_CURRENT_TUMOR_INDEX, i);

            Tumor tumor = new Tumor();

            // if there is pre-processing constant values, set them
            if (options.getConstantTumorValuesPreProcessing() != null)
                for (Entry<String, String> entry : options.getConstantTumorValuesPreProcessing().entrySet())
                    setValue(tumor, entry.getKey(), entry.getValue());

            // execute the tumor rules
            for (NaaccrDataGeneratorTumorRule rule : _tumorRules)
                if (allPropertiesHaveValue(patient, tumor, rule.getRequiredProperties()))
                    rule.execute(tumor, patient, options, context);

            // filter created keys to make sure they are in the layout
            for (Item item : tumor.getItems().stream().filter(item -> isInvalidValidField(item.getNaaccrId())).collect(Collectors.toList()))
                tumor.removeItem(item);

            // if there is post-processing constant values, set them
            if (options.getConstantTumorValuesPostProcessing() != null)
                for (Entry<String, String> entry : options.getConstantTumorValuesPostProcessing().entrySet())
                    setValue(tumor, entry.getKey(), entry.getValue());

            // we are done with this tumor, add it to the result
            patient.addTumor(tumor);
        }

        if (useFullDates) {
            addFullDates(patient);
            for (Tumor tumor : patient.getTumors())
                addFullDates(tumor);
        }

        return patient;
    }

    protected void setValue(AbstractEntity entity, String property, String value) {
        Item item = entity.getItem(property);
        if (item != null)
            item.setValue(value);
        else
            entity.addItem(new Item(property, value));
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
    protected boolean allPropertiesHaveValue(Patient patient, Tumor tumor, List<String> properties) {
        for (String property : properties) {
            String val = patient.getItemValue(property);
            if (StringUtils.isBlank(val) && tumor != null)
                val = tumor.getItemValue(property);
            if (StringUtils.isBlank(val))
                return false;
        }
        return true;
    }

    protected void addFullDates(AbstractEntity entity) {
        for (String yearProp : entity.getItems().stream().map(Item::getNaaccrId).filter(i -> i.endsWith("Year")).collect(Collectors.toList())) {
            String prop = yearProp.replace("Year", "");

            String yearVal = entity.getItemValue(prop + "Year");
            String monthVal = entity.getItemValue(prop + "Month");
            String dayVal = entity.getItemValue(prop + "Day");

            StringBuilder val = new StringBuilder(StringUtils.leftPad(yearVal, 4, '0'));
            if (!StringUtils.isBlank(monthVal))
                val.append(StringUtils.leftPad(monthVal, 2, '0'));
            if (!StringUtils.isBlank(dayVal))
                val.append(StringUtils.leftPad(dayVal, 2, '0'));

            setValue(entity, prop, val.toString());

            entity.removeItem(prop + "Year");
            entity.removeItem(prop + "Month");
            entity.removeItem(prop + "Day");
        }

    }
}
