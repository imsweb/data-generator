/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ObservationRequestSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ObservationRequestSegmentRule() {
        super("observation-request-segment", "Observation Request Segment (NK1)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {
        new Hl7MessageBuilder(message).withSegment("OBR")

                // OBR-1: set ID
                .withField(1, "1");

                // OBR-3: filler order number

                // OBR-10: collector identifier

                // OBR-14: specimen received date/time

                // OBR-16: ordering provider

                // OBR-17: order call back phone

                // OBR-28: results copies to

                // OBR-32: principal result interpreter
    }
}
