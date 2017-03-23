package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class ReportingSourceRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "type-rpt-src";

    private static final String _CRITERIA = "Type of Reporting Source is always set to 1.";

    /**
     * Constructor.
     */
    public ReportingSourceRule() {
        super(ID, "Type of Reporting Source");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("typeOfReportingSource", "1");
    }
}
