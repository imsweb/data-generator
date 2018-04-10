/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;

public class PhysicianRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "physician";

    /**
     * Constructor.
     */
    public PhysicianRule() {
        super(ID, "Physician fields");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if ((options != null) && (options.getPhysicians() != null)) {

            // Pick a random physician for each visit.
            Map<String, String> physicianManaging = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("physicianManagingNpi", physicianManaging.get("npi"));
            record.put("physicianManaging", physicianManaging.get("nameLast") + ", " + physicianManaging.get("nameFirst"));

            Map<String, String> physicianFollowup = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("physicianFollowUpNpi", physicianFollowup.get("npi"));
            record.put("physicianFollowUp", physicianFollowup.get("nameLast") + ", " + physicianFollowup.get("nameFirst"));

            Map<String, String> physicianPrimary = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("physicianPrimarySurgNpi", physicianPrimary.get("npi"));
            record.put("physicianPrimarySurg", physicianPrimary.get("nameLast") + ", " + physicianPrimary.get("nameFirst"));
        }

    }
}