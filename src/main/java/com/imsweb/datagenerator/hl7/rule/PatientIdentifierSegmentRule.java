/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7.rule;

import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorOptions;
import com.imsweb.datagenerator.hl7.NaaccrHl7DataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.UniformRandomValueGenerator;
import com.imsweb.layout.hl7.entity.Hl7Component;
import com.imsweb.layout.hl7.entity.Hl7Field;
import com.imsweb.layout.hl7.entity.Hl7Message;
import com.imsweb.layout.hl7.entity.Hl7RepeatedField;
import com.imsweb.layout.hl7.entity.Hl7Segment;

public class PatientIdentifierSegmentRule extends NaaccrHl7DataGeneratorRule {

    protected static final DistributedRandomValueGenerator _NAME_LAST = new DistributedRandomValueGenerator("frequencies/last_names_white.csv");
    protected static final UniformRandomValueGenerator _NAME_FIRST = new UniformRandomValueGenerator("lists/first_names_male.csv");

    public PatientIdentifierSegmentRule() {
        super("patient-identifier-segment", "Patient Identifier Segment (PID)");
    }

    @Override
    public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options) {
        Hl7Segment segment = new Hl7Segment(message, "PID");

        // PID-1: sequence
        new Hl7Field(segment, 1, "1");

        // PID-3: patient identifier list (providing a medical record number and an SSN)
        Hl7Field pid3Fld = new Hl7Field(segment, 3);
        Hl7RepeatedField pid3Rep1 = new Hl7RepeatedField(pid3Fld);
        new Hl7Component(pid3Rep1, 1, "MR-" + RandomUtils.getRandomStringOfLettersOrDigits(8));
        new Hl7Component(pid3Rep1, 5, "MR");

        Hl7RepeatedField pid3Rep2 = new Hl7RepeatedField(pid3Fld);
        new Hl7Component(pid3Rep2, 1, RandomUtils.getRandomStringOfDigits(9));
        new Hl7Component(pid3Rep2, 5, "SS");

        // PID-5: patient name
        new Hl7Field(segment, 5, _NAME_LAST.getRandomValue(), _NAME_FIRST.getRandomValue());

    }
}
