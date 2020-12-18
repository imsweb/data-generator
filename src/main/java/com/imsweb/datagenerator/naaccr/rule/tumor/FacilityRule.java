/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.provider.facility.FacilityDto;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class FacilityRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "facility";

    /**
     * Constructor.
     */
    public FacilityRule() {
        super(ID, "Facility fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options != null && options.getFacilities() != null && !options.getFacilities().isEmpty()) {
            // Pick a random facility and add it to the record.
            FacilityDto facility = options.getFacilities().get(RandomUtils.nextInt(options.getFacilities().size()));
            setValue(tumor, "npiReportingFacility", facility.getNpi());
        }
    }
}
