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

public class RxTextRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "rx-text";

    public RxTextRule() {
        super(ID, "RX Text fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "rxTextRadiation", RandomTextGeneratorUtil.getRandomText()); // radiation beam
        setValue(tumor, "rxTextRadiationOther", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "rxTextChemo", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "rxTextHormone", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "rxTextBrm", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "rxTextOther", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "rxSummTransplntEndocr", RandomTextGeneratorUtil.getRandomText());
    }
}
