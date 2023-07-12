/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorTextRule;

public class RxTextRule extends NaaccrDataGeneratorTumorTextRule {

    // unique identifier for this rule
    public static final String ID = "rx-text";

    private static final String[] _TEXT_FIELDS = {"rxTextRadiation", "rxTextRadiationOther", "rxTextSurgery", "rxTextChemo", "rxTextHormone", "rxTextBrm", "rxTextOther"};

    public RxTextRule() {
        super(ID, "RX Text fields");
    }

    @Override
    public String[] getNAACCRTextFields() {
        return _TEXT_FIELDS;
    }
}
