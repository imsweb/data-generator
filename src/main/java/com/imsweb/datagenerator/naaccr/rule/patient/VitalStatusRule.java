package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.rule.FrequencyRule;

public class VitalStatusRule extends FrequencyRule {

    // unique identifier for this rule
    public static final String ID = "vital-status";

    private static final String _CRITERIA = "Vital Status is randomly generated based on frequency.";

    /**
     * Constructor.
     */
    public VitalStatusRule() {
        super(ID, "Vital Status", "frequencies/vital_status.csv");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        String value = getRandomValue();

        // recode 4 into 0 if we have to
        if ("4".equals(value) && options != null && options.getVitalStatusDeadValue() != null)
            value = options.getVitalStatusDeadValue();

        record.put("vitalStatus", value);
    }
}
