/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.provider.physician.PhysicianDto;
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
        if (options != null && options.getPhysicians() != null && !options.getPhysicians().isEmpty()) {
            // Pick a random physician for each visit.
            PhysicianDto physicianManaging = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("npiPhysicianManaging", physicianManaging.getNpi());
            record.put("physicianManaging", physicianManaging.getLastName() + ", " + physicianManaging.getFirstName());

            PhysicianDto physicianFollowup = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("npiPhysicianFollowUp", physicianFollowup.getNpi());
            record.put("physicianFollowUp", physicianFollowup.getLastName() + ", " + physicianFollowup.getFirstName());

            PhysicianDto physicianPrimary = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            record.put("npiPhysicianPrimarySurg", physicianPrimary.getNpi());
            record.put("physicianPrimarySurg", physicianPrimary.getLastName() + ", " + physicianPrimary.getFirstName());
        }

    }
}