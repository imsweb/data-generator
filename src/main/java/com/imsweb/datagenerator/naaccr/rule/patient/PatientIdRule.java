package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.naaccrxml.entity.Patient;

@SuppressWarnings("java:S2160")
public class PatientIdRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "patient-id-number";

    // current counter value
    private int _currentValue;

    /**
     * Constructor.
     */
    public PatientIdRule() {
        super(ID, "Patient ID Number");
        _currentValue = 1;
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "patientIdNumber", String.format("%08d", _currentValue++));
    }
}
