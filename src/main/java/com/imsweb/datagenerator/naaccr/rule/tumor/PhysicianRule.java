/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.PhysicianDto;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class PhysicianRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "physician";

    /**
     * Constructor.
     */
    public PhysicianRule() {
        super(ID, "Physician fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options != null && options.getPhysicians() != null && !options.getPhysicians().isEmpty()) {
            // Pick a random physician for each visit.
            PhysicianDto physicianManaging = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            setValue(tumor, "npiPhysicianManaging", physicianManaging.getNpi());
            setValue(tumor, "physicianManaging", physicianManaging.getLastName() + ", " + physicianManaging.getFirstName());

            PhysicianDto physicianFollowup = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            setValue(tumor, "npiPhysicianFollowUp", physicianFollowup.getNpi());
            setValue(tumor, "physicianFollowUp", physicianFollowup.getLastName() + ", " + physicianFollowup.getFirstName());

            PhysicianDto physicianPrimary = options.getPhysicians().get(RandomUtils.nextInt(options.getPhysicians().size()));
            setValue(tumor, "npiPhysicianPrimarySurg", physicianPrimary.getNpi());
            setValue(tumor, "physicianPrimarySurg", physicianPrimary.getLastName() + ", " + physicianPrimary.getFirstName());
        }

    }
}