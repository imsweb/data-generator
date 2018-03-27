/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class NPIRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "facility-npi";

    /**
     * Constructor.
     */
    public NPIRule() {
        super(ID, "Facility NPI");
    }

    //@Override
    //public List<String> getRequiredProperties() {
    //    return Arrays.asList("birthDateYear", "birthDateMonth", "birthDateDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    //}

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {

    }
}
