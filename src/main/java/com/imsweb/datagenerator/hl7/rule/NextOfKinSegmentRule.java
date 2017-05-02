/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.UniformRandomValueGenerator;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class NextOfKinSegmentRule extends NaaccrHl7DataGeneratorRule {

    protected static final DistributedRandomValueGenerator _NAME_LAST = new DistributedRandomValueGenerator("frequencies/last_names_white.csv");

    protected static final UniformRandomValueGenerator _NAME_FIRST = new UniformRandomValueGenerator("lists/first_names_female.csv");

    public NextOfKinSegmentRule() {
        super("next-of-kin-segment", "Next of Kin Segment (NK1)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {
        new Hl7MessageBuilder(message).withSegment("NK1")

                // NK1-1: set ID
                .withField(1, "1")

                // NK1-2: name
                .withField(2, _NAME_LAST.getRandomValue(), _NAME_FIRST.getRandomValue(), RandomUtils.getRandomStringOfLetters(1))

                // NK1-3: relationship
                .withField(3, "SPO", "spouse", "HL70063");

                // NK1-4: address

                // NK1-5: phone
    }
}
