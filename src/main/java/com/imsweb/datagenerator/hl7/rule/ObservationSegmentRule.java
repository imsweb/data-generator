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
        new Hl7MessageBuilder(message).withSegment("OBX")

                // OBX-1: set ID
                .withField(1, "1")

                // OBX-2: value type (CE for coded value, ST for short string text, FT for formatted text, TX for text data)
                .withField(2, "TX")

                // OBX-3: observation identifier
                .withField(3, "22637-3", "Path report final diagnosis", "LN")

                // OBX-5: observation value
                .withField(5, "Malignant lymphoma, small B-cell type with plasmacytic differentiation and crystal-storing histiocytosis")

                // OBX-11: observation result status
                .withField(11, "F")

                // finalize the build
                .build();
    }
}
