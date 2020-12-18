package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class DateOfInitialRxRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "date-of-initial-rx";

    /**
     * Constructor.
     */
    public DateOfInitialRxRule() {
        super(ID, "Date of Initial RX");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        // for now this is set to the DX date...
        setValue(tumor, "dateInitialRxSeerYear", tumor.getItemValue("dateOfDiagnosisYear"));
        setValue(tumor, "dateInitialRxSeerMonth", tumor.getItemValue("dateOfDiagnosisMonth"));
        setValue(tumor, "dateInitialRxSeerDay", tumor.getItemValue("dateOfDiagnosisDay"));
    }
}
