/*
 * Copyright (C) 2022 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.StagingUtils;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_CURRENT_TUMOR_INDEX;
import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SITE_FREQ_MAP;

public class StagingInputRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "staging-input";

    /**
     * Constructor.
     */
    public StagingInputRule() {
        super(ID, "Staging input fields");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Arrays.asList("primarySite", "histologicTypeIcdO3", "dateOfDiagnosisYear");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        Map<Integer, SiteFrequencyDto> siteFreqMap = (Map<Integer, SiteFrequencyDto>)context.get(CONTEXT_FLAG_SITE_FREQ_MAP);
        if (siteFreqMap != null) {
            SiteFrequencyDto siteFrequency = siteFreqMap.get((Integer)context.get(CONTEXT_FLAG_CURRENT_TUMOR_INDEX));
            if (siteFrequency != null) {
                Map<String, String> randomValidValues = null;

                int dxYear = Integer.parseInt(tumor.getItemValue("dateOfDiagnosisYear"));
                if (dxYear >= 2004 && dxYear <= 2015)
                    randomValidValues = StagingUtils.getCsValues(siteFrequency.getCsSchemaId());
                else if (dxYear >= 2016 && dxYear <= 2017)
                    randomValidValues = StagingUtils.getTnmValues(siteFrequency.getTnmSchemaId());
                else if (dxYear >= 2018)
                    randomValidValues = StagingUtils.getEodValues(siteFrequency.getEodSchemaId());

                if (randomValidValues != null)
                    for (Entry<String, String> entry : randomValidValues.entrySet())
                        setValue(tumor, entry.getKey(), entry.getValue());
            }
        }
    }

    // the framework doesn't support proper documentation, but maybe one day it will; I am adding this while it's fresh in my memory...
    public String getDescription () {
        return "This rule assigns a value to the staging input field.\n"
                + "\n"
                + "Which staging framework is used is based on the DX year:\n"
                + " - 2004-2015: CS\n"
                + " - 2016-2017: TNM\n"
                + " - 2018+: EOD\n"
                + "\n"
                + "The schema is determined based on the assgned site/histology.\n"
                + "\n"
                + "Some combinations of site/histolgy correspond to more than one schema; a discriminator should be used to \n"
                + "uniquely identify the schema but this framework doesn't use discriminators, it just takes the \"first\" schema found.\n"
                + "\n"
                + "Once a schema has been identified, every input field is assigned wiht one of its \"valid\" value.\n"
                + "Any of the valid values for a given input has the same probability to be picked.";
    }
}
