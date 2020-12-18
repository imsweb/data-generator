package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.naaccrxml.entity.Patient;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SEX;

public class SexRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "sex";

    /**
     * Constructor.
     */
    public SexRule() {
        super(ID, "Sex");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "sex", (String)context.getOrDefault(CONTEXT_FLAG_SEX, DistributionUtils.getSex()));
    }
}
