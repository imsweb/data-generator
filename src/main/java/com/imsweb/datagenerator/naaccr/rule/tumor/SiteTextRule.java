/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

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
        setValue(tumor, "textPrimarySiteTitle", "");
        setValue(tumor, "textHistologyTitle", "");
        setValue(tumor, "textStaging", "");
        setValue(tumor, "textSurgery", ""); // rxTextSurgery
        setValue(tumor, "textRemarks", "");
//        setValue(tumor, "", ""); // path comments
//        setValue(tumor, "", ""); // path formal dx
//        setValue(tumor, "", ""); // path full text
    }
}
