/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ControlSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ControlSegmentRule() {
        super("control-segment", "Control Segment (MSH)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {
        new Hl7MessageBuilder(message).withSegment("MSH")

                // MSH-3: sending application
                .withField(3, "IMS Data Generator")

                // MSH-4: sending facility
                .withField(4, "FAKE PATH LAB", "3D9999999", "CLIA");

                // MSH-7: message date (set by the layout framework)

                // MSH-12: version (set by the layout framework)
    }
}
