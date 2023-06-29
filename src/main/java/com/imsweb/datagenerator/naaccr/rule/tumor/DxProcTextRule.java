/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

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
        setValue(tumor, "textDxProcLabTests", "");
        setValue(tumor, "textDxProcOp", "");
        setValue(tumor, "textDxProcPath", "");
        setValue(tumor, "textDxProcPe", "");
        setValue(tumor, "textDxProcScopes", "");
        setValue(tumor, "textDxProcXRayScan", "");
    }
}
