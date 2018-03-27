/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class NameRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "facility-name";

    /**
     * Constructor.
     */
    public NameRule() {
        super(ID, "Facility Name");
    }

    //@Override
    //public List<String> getRequiredProperties() {
    //    return Arrays.asList("birthDateYear", "birthDateMonth", "birthDateDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    //}

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {

    }
}
