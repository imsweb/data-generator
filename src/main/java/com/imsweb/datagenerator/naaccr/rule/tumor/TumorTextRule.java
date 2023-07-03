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

public class TumorTextRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-text";

    public TumorTextRule() {
        super(ID, "Tumor Text fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "textStaging", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textRemarks", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "ehrReporting", RandomTextGeneratorUtil.getRandomText());
    }
}
