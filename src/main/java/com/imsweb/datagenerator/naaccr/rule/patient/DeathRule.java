package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class DeathRule extends NaaccrDataGeneratorRule {

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        if (record.get("vitalStatus").equals("1")) {
            record.put("causeOfDeath", "0000");
            record.put("icdRevisionNumber", "0");
        }
    }
}
