package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.rule.FrequencyRule;

public class HispanicOriginRule extends FrequencyRule {

    // unique identifier for this rule
    public static final String ID = "spanish-hispanic-origin";

    /**
     * Constructor.
     */
    public HispanicOriginRule() {
        super(ID, "Spanish/Hispanic Origin", "frequencies/hispanic_origin.csv");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("spanishHispanicOrigin", getRandomValue());
    }
}
