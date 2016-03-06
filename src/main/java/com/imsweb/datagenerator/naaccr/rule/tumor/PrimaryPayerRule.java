package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class PrimaryPayerRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "primary-payer-at-dx";

    /**
     * Constructor.
     */
    public PrimaryPayerRule() {
        super(ID, "Primary Payer at DX");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("primaryPayerAtDx", "99");
    }
}
