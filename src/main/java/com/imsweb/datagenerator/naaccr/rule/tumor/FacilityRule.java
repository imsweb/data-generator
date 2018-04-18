/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;

public class FacilityRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "facility";

    /**
     * Constructor.
     */
    public FacilityRule() {
        super(ID, "Facility fields");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options != null && options.getFacilities() != null && !options.getFacilities().isEmpty()) {
            // Pick a random facility and add it to the record.
            FacilityFrequencyDto facility = options.getFacilities().get(RandomUtils.nextInt(options.getFacilities().size()));
            record.put("reportingFacilityNpi", facility.getNpi());
        }
    }
}
