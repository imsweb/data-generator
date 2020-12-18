package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class PrimaryPayerRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "primary-payer-at-dx";

    /**
     * Constructor.
     */
    public PrimaryPayerRule() {
        super(ID, "Primary Payer at DX");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "primaryPayerAtDx", "99");
    }
}
