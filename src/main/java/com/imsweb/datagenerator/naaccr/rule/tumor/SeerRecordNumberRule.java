package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class SeerRecordNumberRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "seer-rec-num";

    /**
     * Constructor.
     */
    public SeerRecordNumberRule() {
        super(ID, "SEER Record Number");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "seerRecordNumber", String.format("%02d", patient.getTumors().size() + 1));
    }
}
