/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.layout.hl7.entity.Hl7Field;
import com.imsweb.layout.hl7.entity.Hl7Message;
import com.imsweb.layout.hl7.entity.Hl7Segment;

public class ControlSegmentRule extends NaaccrHl7DataGeneratorRule {

    public ControlSegmentRule() {
        super("control-segment", "Control Segment (MSH)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {
        Hl7Segment segment = new Hl7Segment(message, "MSH");
        
        // sending application
        new Hl7Field(segment, 3, "IMS Data Generator");
        
        // sending facility
        new Hl7Field(segment, 4, "FAKE PATH LAB", "3D9999999", "CLIA");
    }
}
