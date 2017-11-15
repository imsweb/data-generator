package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class PatientIdRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "patient-id-number";

    private static final String _DESCRIPTION = "Patient ID starts at 00000001 and is incremented by 1 for each new patient.";

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
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("patientIdNumber", String.format("%08d", _currentValue++));
    }
}
