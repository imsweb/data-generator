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

public class DxProcTextRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "dx-proc-text";

    public DxProcTextRule() {
        super(ID, "Diagnostic Procedure Text fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "textDxProcLabTests", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textDxProcOp", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textDxProcPath", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textDxProcPe", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textDxProcScopes", RandomTextGeneratorUtil.getRandomText());
        setValue(tumor, "textDxProcXRayScan", RandomTextGeneratorUtil.getRandomText());
    }
}
