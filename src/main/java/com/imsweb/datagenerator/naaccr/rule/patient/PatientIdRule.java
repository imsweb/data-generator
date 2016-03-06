package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class PatientIdRule extends NaaccrDataGeneratorRule {

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("patientIdNumber", String.format("%08d", _currentValue++));
    }
}
