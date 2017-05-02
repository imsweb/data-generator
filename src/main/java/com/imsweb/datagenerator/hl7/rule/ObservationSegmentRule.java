/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ObservationSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ObservationSegmentRule() {
        super("observation-segment", "Observation/Result Segment (OBX)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {

        // OBX-1: set ID
        // OBX-2: value type (CE for coded value, ST for short string text, FT for formatted text, TX for text data)
        // OBX-3: observation identifier
        // OBX-5: observation value
        // OBX-11: observation result status (F=final)

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "1")
                .withField(2, "TX")
                .withField(3, "22633-2", "nature of specimen", "LN")
                .withField(5, "Bone marrow.")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "2")
                .withField(2, "TX")
                .withField(3, "", "", "LN")
                .withField(5, "")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "3")
                .withField(2, "TX")
                .withField(3, "", "", "LN")
                .withField(5, "")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "4")
                .withField(2, "TX")
                .withField(3, "", "", "LN")
                .withField(5, "")
                .withField(11, "F");

        new Hl7MessageBuilder(message).withSegment("OBX")
                .withField(1, "5")
                .withField(2, "TX")
                .withField(3, "", "", "LN")
                .withField(5, "")
                .withField(11, "F");
    }
}
