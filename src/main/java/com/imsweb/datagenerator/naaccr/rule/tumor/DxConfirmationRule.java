package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class DxConfirmationRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "dx-confirmation";

    /**
     * Constructor.
     */
    public DxConfirmationRule() {
        super(ID, "Diagnostic Confirmation");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        record.put("diagnosticConfirmation", "1");
    }
}
