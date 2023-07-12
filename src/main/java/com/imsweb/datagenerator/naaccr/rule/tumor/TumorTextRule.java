/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorTextRule;

public class TumorTextRule extends NaaccrDataGeneratorTumorTextRule {

    // unique identifier for this rule
    public static final String ID = "tumor-text";

    private static final String[] _TEXT_FIELDS = {"textStaging", "textRemarks", "ehrReporting"};

    public TumorTextRule() {
        super(ID, "Tumor Text fields");
    }

    @Override
    public String[] getNAACCRTextFields() {
        return _TEXT_FIELDS;
    }
}
