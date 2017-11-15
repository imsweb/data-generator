package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class DeathRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "death";

    private static final String _DESCRIPTION = "If Vital Status was set to 1, Cause of Death will be set to 0000 and ICD Revision Number will be set to 0.<br/>"
            + "Otherwise these fields are not set";

    /**
     * Constructor.
     */
    public DeathRule() {
        super(ID, "Cause of death and ICD Revision Number");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("vitalStatus");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        if ("1".equals(record.get("vitalStatus"))) {
            record.put("causeOfDeath", "0000");
            record.put("icdRevisionNumber", "0");
        }
    }
}
