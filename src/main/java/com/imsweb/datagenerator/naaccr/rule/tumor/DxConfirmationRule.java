package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DxConfirmationRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "dx-confirmation";

    /**
     * Constructor.
     */
    public DxConfirmationRule() {
        super(ID, "Diagnostic Confirmation");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "diagnosticConfirmation", "1");
    }
}
