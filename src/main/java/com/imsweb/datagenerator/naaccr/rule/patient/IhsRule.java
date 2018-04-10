package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class IhsRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "ihs";

    /**
     * Constructor.
     */
    public IhsRule() {
        super(ID, "IHS");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        record.put("ihs", "");
    }
}
