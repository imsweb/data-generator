package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class VitalStatusRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "vital-status";

    /**
     * Constructor.
     */
    public VitalStatusRule() {
        super(ID, "Vital Status");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        String value = DistributionUtils.getVitalStatus();

        // recode 4 into 0 if we have to
        if ("0".equals(value) && options != null && options.getVitalStatusDeadValue() != null)
            value = options.getVitalStatusDeadValue();

        setValue(patient, "vitalStatus", value);
    }
}
