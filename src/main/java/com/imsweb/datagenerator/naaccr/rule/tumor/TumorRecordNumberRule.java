package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class TumorRecordNumberRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-record-number";

    /**
     * Constructor.
     */
    public TumorRecordNumberRule() {
        super(ID, "Tumor Record Number");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "tumorRecordNumber", String.format("%02d", patient.getTumors().size() + 1));
    }
}
