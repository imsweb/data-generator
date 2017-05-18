/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class CommonOrderSegmentRule extends NaaccrHl7DataGeneratorRule {

    public CommonOrderSegmentRule() {
        super("common-order-segment", "Common Order Segment (ORC)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {
        new Hl7MessageBuilder(message).withSegment("ORC")

                // ORC-1: order control
                .withField(1, "1");

                // ORC-21: ordering facility name

                // ORC-22: ordering facility address

                // ORC-23: ordering facility phone

                // ORC-24: ordering provider address
    }
}
