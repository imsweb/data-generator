/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;

public class NPIRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "physician-npi";

    /**
     * Constructor.
     */
    public NPIRule() {
        super(ID, "Physician NPI");
    }

    //@Override
    //public List<String> getRequiredProperties() {
    //    return Arrays.asList("birthDateYear", "birthDateMonth", "birthDateDay", "dateOfDiagnosisYear", "dateOfDiagnosisMonth", "dateOfDiagnosisDay");
    //}

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {

    }
}
