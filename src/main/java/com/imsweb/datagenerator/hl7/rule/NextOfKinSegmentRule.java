/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class NextOfKinSegmentRule extends NaaccrHl7DataGeneratorRule {

    public NextOfKinSegmentRule() {
        super("next-of-kin-segment", "Next of Kin Segment (NK1)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {

        // TODO use the ethnicity of the PID to generate a name for same one here
        // TODO invert the sex in PID and always use spouse

        new Hl7MessageBuilder(message).withSegment("NK1")

                // NK1-1: set ID
                .withField(1, "1")

                // NK1-2: name
                .withField(2, DistributionUtils.getNameLast(), DistributionUtils.getNameFirst(), RandomUtils.getRandomStringOfLetters(1))

                // NK1-3: relationship
                .withField(3, "SPO", "spouse", "HL70063")

                // NK1-4: address

                // NK1-5: phone
                .withField(5)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7));
    }
}
