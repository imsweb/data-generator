/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class RxTextRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "rx-text";

    public RxTextRule() {
        super(ID, "RX Text fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "rxTextRadiation", "");
        setValue(tumor, "rxTextRadiationOther", "");
        setValue(tumor, "rxTextChemo", "");
        setValue(tumor, "rxTextHormone", "");
        setValue(tumor, "rxTextBrm", "");
        setValue(tumor, "rxTextOther", "");
    }
}
