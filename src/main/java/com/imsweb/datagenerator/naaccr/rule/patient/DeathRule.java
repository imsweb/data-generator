package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.naaccrxml.entity.Patient;

public class DeathRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "death";

    /**
     * Constructor.
     */
    public DeathRule() {
        super(ID, "Cause of death and ICD Revision Number");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("vitalStatus");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if ("1".equals(patient.getItemValue("vitalStatus"))) {
            setValue(patient, "causeOfDeath", "0000");
            setValue(patient, "icdRevisionNumber", "0");
        }
    }
}
