package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class SeerTypeOfFollowUpRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "seer-type-of-fup";

    /**
     * Constructor.
     */
    public SeerTypeOfFollowUpRule() {
        super(ID, "SEER Type of Follow Up");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "seerTypeOfFollowUp", "2");
    }
}
