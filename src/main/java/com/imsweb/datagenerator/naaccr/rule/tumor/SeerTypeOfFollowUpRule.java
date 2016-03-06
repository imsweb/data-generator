package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class SeerTypeOfFollowUpRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "seer-type-of-fup";

    /**
     * Constructor.
     */
    public SeerTypeOfFollowUpRule() {
        super(ID, "SEER Type of Follow Up");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("seerTypeOfFollowUp", "2");
    }
}
