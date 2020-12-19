package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DateOfLastContactRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-last-contact";

    /**
     * Constructor.
     */
    public DateOfLastContactRule() {
        super(ID, "Date of Last Contact");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // for now this is set to the DX date (they are assigned in order, so the DOLC will end up being the last one)
        setValue(patient, "dateOfLastContactYear", tumor.getItemValue("dateOfDiagnosisYear"));
        setValue(patient, "dateOfLastContactMonth", tumor.getItemValue("dateOfDiagnosisMonth"));
        setValue(patient, "dateOfLastContactDay", tumor.getItemValue("dateOfDiagnosisDay"));
    }
}
