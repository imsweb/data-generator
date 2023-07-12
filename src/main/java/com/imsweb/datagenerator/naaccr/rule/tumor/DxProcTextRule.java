/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorTextRule;

public class DxProcTextRule extends NaaccrDataGeneratorTumorTextRule {

    // unique identifier for this rule
    public static final String ID = "dx-proc-text";

    private static final String[] _TEXT_FIELDS = {"textDxProcLabTests", "textDxProcOp", "textDxProcPath", "textDxProcPe", "textDxProcScopes", "textDxProcXRayScan"};

    public DxProcTextRule() {
        super(ID, "Diagnostic Procedure Text fields");
    }

    @Override
    public String[] getNAACCRTextFields() {
        return _TEXT_FIELDS;
    }
}
