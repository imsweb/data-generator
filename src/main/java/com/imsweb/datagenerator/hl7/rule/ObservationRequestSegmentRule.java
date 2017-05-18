/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ObservationRequestSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ObservationRequestSegmentRule() {
        super("observation-request-segment", "Observation Request Segment (OBR)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {
        new Hl7MessageBuilder(message).withSegment("OBR")

                // OBR-1: set ID
                .withField(1, "1")

                // OBR-3: filler order number
                .withField(3, RandomUtils.getRandomStringOfDigits(6))

                // OBR-4: universal service ID
                .withField(4, "11529-5", "SURGICAL PATH REPORT", "LN")

                // OBR-10: collector identifier
                .withField(10, RandomUtils.getRandomStringOfDigits(5), DistributionUtils.getNameLast(), DistributionUtils.getNameFirst())

                // OBR-14: specimen received date/time
                .withField(14, DateTimeFormatter.ofPattern("yyyyMMdd").format(RandomUtils.getRandomDateBetween(LocalDate.now().minusYears(1L), LocalDate.now())))

                // OBR-16: ordering provider
                .withField(16)
                .withComponent(2, DistributionUtils.getNameLast())
                .withComponent(3, DistributionUtils.getNameFirst())
                .withComponent(4, RandomUtils.getRandomStringOfLetters(1))

                // OBR-17: order call back phone
                .withField(17)
                .withComponent(6, RandomUtils.getRandomStringOfDigits(3))
                .withComponent(7, RandomUtils.getRandomStringOfDigits(7))

                // OBR-32: principal result interpreter
                .withField(32)
                .withComponent(1, null, DistributionUtils.getNameLast(), DistributionUtils.getNameFirst(), RandomUtils.getRandomStringOfLetters(1));
    }
}
