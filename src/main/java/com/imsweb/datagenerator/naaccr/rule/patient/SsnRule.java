package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class SsnRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "social-security-number";

    /**
     * Constructor.
     */
    public SsnRule() {
        super(ID, "Social Security Number");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        // three digit area code (001-649)
        String areaCode = String.format("%03d", RandomUtils.nextInt(648) + 1);
        // two digit group number (01-99)
        String groupNumber = String.format("%02d", RandomUtils.nextInt(98) + 1);
        // four single-digit serial numbers (0001-9999)
        String serialNumbers = String.format("%04d", RandomUtils.nextInt(9998) + 1);
        setValue(patient, "socialSecurityNumber", areaCode + groupNumber + serialNumbers);
    }
}
