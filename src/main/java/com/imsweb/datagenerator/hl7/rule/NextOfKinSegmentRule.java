/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class NextOfKinSegmentRule extends NaaccrHl7DataGeneratorRule {

    public NextOfKinSegmentRule() {
        super("next-of-kin-segment", "Next of Kin Segment (NK1)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {

        // get ethnicity/race from the context so we use the same ones as the patient
        String hispanicOrigin = (String)context.get("hispanicOrigin"), race = (String)context.get("race");

        // address
        String[] address = null;
        if (options != null && options.getState() != null) {
            CityFrequencyDto dto = DistributionUtils.getCity(options.getState());
            address = new String[] {DistributionUtils.getStreetName(), null, dto.getCity(), dto.getState(), dto.getZip()};
        }

        new Hl7MessageBuilder(message).withSegment("NK1")

                // NK1-1: set ID
                .withField(1, "1")

                // NK1-2: name
                .withField(2, DistributionUtils.getNameLast(hispanicOrigin, race), DistributionUtils.getNameFirst(), RandomUtils.getRandomStringOfLetters(1))

                // NK1-3: relationship
                .withField(3, "SPO", "spouse", "HL70063")

                // NK1-4: address
                .withField(4, address)

                // NK1-5: phone
                .withField(5)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7));
    }
}
