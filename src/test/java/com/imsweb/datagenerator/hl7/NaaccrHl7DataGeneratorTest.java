/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import testing.TestingUtils;

import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.hl7.NaaccrHl7Layout;
import com.imsweb.layout.hl7.entity.Hl7Message;

public class NaaccrHl7DataGeneratorTest {

    @Test
    public void testGenerator() throws IOException {

        // get the HL7 layout
        NaaccrHl7Layout layout = (NaaccrHl7Layout)LayoutFactory.getLayout(LayoutFactory.LAYOUT_ID_NAACCR_HL7_2_5_1);

        // create generator
        NaaccrHl7DataGenerator generator = new NaaccrHl7DataGenerator(layout);

        // we will use this target file
        File file = TestingUtils.createFile("naaccr-hl7-test.txt");

        // test with no options
        generator.generateFile(file, 1, null);
        Hl7Message msg = layout.readAllMessages(file).get(0);
        Assert.assertNotNull(msg.getSegment("PID").getField(5).getValue());
        Assert.assertNull(msg.getSegment("PID").getField(11).getValue());

        // test state option
        NaaccrHl7DataGeneratorOptions options = new NaaccrHl7DataGeneratorOptions();
        options.setState("MD");
        generator.generateFile(file, 3, options);
        msg = layout.readAllMessages(file).get(2);
        Assert.assertNotNull(msg.getSegment("PID").getField(5).getValue());
        Assert.assertNotNull(msg.getSegment("PID").getField(11).getValue());

        // test codes site option
        options.setGenerateSiteAndMorphology(true);
        generator.generateFile(file, 1, options);
        msg = layout.readAllMessages(file).get(0);
        Assert.assertTrue(msg.getSegment("OBX", 5).getField(5).getValue().matches("C\\d+"));
        Assert.assertTrue(msg.getSegment("OBX", 6).getField(5).getValue().matches("M-\\d+"));
    }

}
