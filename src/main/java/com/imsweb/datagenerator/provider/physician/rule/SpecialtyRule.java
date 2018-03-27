/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class SpecialtyRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "physician-specialty";

    /**
     * Constructor.
     */
    public SpecialtyRule() {
        super(ID, "Physician Specialty");
    }

    //@Override
    //public List<String> getRequiredProperties() {
    //    return Arrays.asList("birthDateYear", "birthDateMonth", "birthDateDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    //}

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {

    }
}
