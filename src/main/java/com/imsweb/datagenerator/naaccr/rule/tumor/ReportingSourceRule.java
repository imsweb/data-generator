package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class ReportingSourceRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "type-rpt-src";

    /**
     * Constructor.
     */
    public ReportingSourceRule() {
        super(ID, "Type of Reporting Source");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {
        record.put("typeOfReportingSource", "1");
    }
}
