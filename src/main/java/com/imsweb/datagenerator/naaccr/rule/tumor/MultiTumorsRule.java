package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class MultiTumorsRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "multi-tumors";

    /**
     * Constructor.
     */
    public MultiTumorsRule() {
        super(ID, "Multiple Tumors fields");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {

        // don't assign anything prior to 2007
        if (!inDxYearRange(record, 2007, null))
            return;

        // for now this is set to the DX date...
        record.put("dateOfMultipleTumorsYear", record.get("dateOfDiagnosisYear"));
        record.put("dateOfMultipleTumorsMonth", record.get("dateOfDiagnosisMonth"));
        record.put("dateOfMultipleTumorsDay", record.get("dateOfDiagnosisDay"));

        record.put("multiTumorRptAsOnePrim", "99");
        record.put("multiplicityCounter", "99");
    }
}
