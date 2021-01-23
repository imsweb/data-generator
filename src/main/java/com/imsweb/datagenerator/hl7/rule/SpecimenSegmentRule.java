/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class SpecimenSegmentRule extends NaaccrHl7DataGeneratorRule {

    public SpecimenSegmentRule() {
        super("specimen-segment", "Specimen Segment (SPM)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {

        new Hl7MessageBuilder(message).withSegment("SPM")

                // SPM-1: order control
                .withField(1, "1")

                // SPM-2: specimen ID
                .withField(2, RandomUtils.getRandomStringOfDigits(2) + "-" + RandomUtils.getRandomStringOfDigits(5))

                // SPM-4: Specimen Type
                .withField(4, "TISS^Tissue");
    }
}
