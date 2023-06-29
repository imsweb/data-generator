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

public class SiteTextRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "site-text";

    public SiteTextRule() {
        super(ID, "Site Text fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "textPrimarySiteTitle", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textHistologyTitle", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textStaging", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textSurgery", RandomTextGeneratorUtil.getRandomText()); // rxTextSurgery
        setValue(tumor, "textRemarks", RandomTextGeneratorUtil.getRandomText());
//        setValue(tumor, "", ""); // path comments
//        setValue(tumor, "", ""); // path formal dx
//        setValue(tumor, "", ""); // path full text
    }
}
