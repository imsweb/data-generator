package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class TelephoneRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "telephone";

    /**
     * Constructor.
     */
    public TelephoneRule() {
        super(ID, "Telephone");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "telephone", RandomUtils.getRandomStringOfDigits(10));
    }
}
