package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class DateOfLastContactRule extends NaaccrDataGeneratorRule {

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        // for now this is set to the DX date...
        record.put("dateOfLastContactYear", record.get("dateOfDiagnosisYear"));
        record.put("dateOfLastContactMonth", record.get("dateOfDiagnosisMonth"));
        record.put("dateOfLastContactDay", record.get("dateOfDiagnosisDay"));

        // update DOLC for previously generated tumors to match this tumor
        for (Map<String, String> otherRecord : otherRecords) {
            otherRecord.put("dateOfLastContactYear", record.get("dateOfLastContactYear"));
            otherRecord.put("dateOfLastContactMonth", record.get("dateOfLastContactMonth"));
            otherRecord.put("dateOfLastContactDay", record.get("dateOfLastContactDay"));
        }
    }
}
