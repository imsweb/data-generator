package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class PrimaryPayerRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "primary-payer-at-dx";

    private static final String _DESCRIPTION = "Primary Payer at DX is alway set to 99.";

    /**
     * Constructor.
     */
    public PrimaryPayerRule() {
        super(ID, "Primary Payer at DX");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("primaryPayerAtDx", "99");
    }
}
