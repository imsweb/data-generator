package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

public class VitalStatusRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "vital-status";

    /**
     * Constructor.
     */
    public VitalStatusRule() {
        super(ID, "Vital Status");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {
        String value = DistributionUtils.getVitalStatus();

        // recode 4 into 0 if we have to
        if ("4".equals(value) && options != null && options.getVitalStatusDeadValue() != null)
            value = options.getVitalStatusDeadValue();

        record.put("vitalStatus", value);
    }
}
