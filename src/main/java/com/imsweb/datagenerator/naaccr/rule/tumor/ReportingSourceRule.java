package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class ReportingSourceRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "type-rpt-src";

    /**
     * Constructor.
     */
    public ReportingSourceRule() {
        super(ID, "Type of Reporting Source");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "typeOfReportingSource", "1");
    }
}
