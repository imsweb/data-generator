package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class RaceRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "race";

    /**
     * Constructor.
     */
    public RaceRule() {
        super(ID, "Race 1-5");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(patient, "race1", DistributionUtils.getRace());
        // if race1 is 99 set race2 - race5 to 99, else set them to 88
        String otherRaceCode = patient.getItemValue("race1").equals("99") ? "99" : "88";
        setValue(patient, "race2", otherRaceCode);
        setValue(patient, "race3", otherRaceCode);
        setValue(patient, "race4", otherRaceCode);
        setValue(patient, "race5", otherRaceCode);
    }
}
