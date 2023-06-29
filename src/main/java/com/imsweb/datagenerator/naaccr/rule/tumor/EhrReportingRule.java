/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.RandomTextGeneratorUtil;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class EhrReportingRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "ehr-reporting";

    public EhrReportingRule() {
        super(ID, "EHR Reporting");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "ehrReporting", RandomTextGeneratorUtil.getRandomText());
    }
}
