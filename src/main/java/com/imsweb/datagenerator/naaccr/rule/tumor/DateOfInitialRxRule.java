package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class DateOfInitialRxRule extends NaaccrDataGeneratorRule {

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        // for now this is set to the DX date...
        record.put("dateInitialRxSeerYear", record.get("dateOfDiagnosisYear"));
        record.put("dateInitialRxSeerMonth", record.get("dateOfDiagnosisMonth"));
        record.put("dateInitialRxSeerDay", record.get("dateOfDiagnosisDay"));
    }
}
