/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.Hl7MessageBuilder;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class ControlSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ControlSegmentRule() {
        super("control-segment", "Control Segment (MSH)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {
        new Hl7MessageBuilder(message).withSegment("MSH")

                // MSH-1: Field Separator
                .withField(1, "|")

                // MSH2: Encoding Character
                .withField(2, "^~\\&")

                // MSH-3: sending application
                .withField(3, "IMS Data Generator")

                // MSH-4: sending facility
                .withField(4, "FAKE PATH LAB", "3D9999999", "CLIA")

                // MSH-7: message date
                .withField(7, DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS").format(LocalDateTime.now()))

                // MSH-9: message type
                .withField(9, "ORU", "R01", "ORU_R01")

                // MSH-10: message control ID
                .withField(10, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()))

                // MSH-11: processing ID
                .withField(11, "P")

                // MSH-12: version ID
                .withField(12, "2.5.1")

                // MSH-17: country code
                .withField(17, "US")

                // MSH-21: Message Profile Identifier
                .withField(21, "VOL_V_50_ORU_R01", "NAACCR_CP");

    }
}
