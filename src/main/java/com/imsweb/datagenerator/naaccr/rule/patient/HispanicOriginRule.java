package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class HispanicOriginRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "spanish-hispanic-origin";

    /**
     * Constructor.
     */
    public HispanicOriginRule() {
        super(ID, "Spanish/Hispanic Origin");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "spanishHispanicOrigin", DistributionUtils.getHispanicOrigin());
    }
}
