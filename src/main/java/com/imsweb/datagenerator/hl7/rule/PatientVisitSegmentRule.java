/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class PatientVisitSegmentRule extends NaaccrHl7DataGeneratorRule {

    public PatientVisitSegmentRule() {
        super("patient-visit-segment", "Patient Visit Segment (PV1)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {
        new Hl7MessageBuilder(message).withSegment("PV1")

                // PV1-1: set ID
                .withField(1, "1")

                // PV1-2: patient class
                .withField(2, "N")

                // PV1-7: attending doctor
                .withField(7, DistributionUtils.getNameLast() + ", " + DistributionUtils.getNameFirst())

                // PV1-8: referring doctor
                .withField(7, DistributionUtils.getNameLast() + ", " + DistributionUtils.getNameFirst());
    }
}
