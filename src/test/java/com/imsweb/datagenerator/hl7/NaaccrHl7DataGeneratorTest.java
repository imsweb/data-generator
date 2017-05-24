/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
        Assert.assertNotNull(generator.getId());
        Assert.assertNotNull(generator.generateMessage());

        // we will use this target file
        File file = TestingUtils.createFile("naaccr-hl7-test.txt");

        // test with no options
        generator.generateFile(file, 1);
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

        // let's switch to a compress file
        file = TestingUtils.createFile("naaccr-hl7-test.txt.gz");

        // test codes site option
        options.setGenerateSiteAndMorphology(true);
        generator.generateFile(file, 1, options);
        msg = layout.readAllMessages(file).get(0);
        Assert.assertTrue(msg.getSegment("OBX", 5).getField(5).getValue().matches("C\\d+"));
        Assert.assertTrue(msg.getSegment("OBX", 6).getField(5).getValue().matches("M-\\d+"));
    }

    @Test
    public void testGeneratorExtension() {
        NaaccrHl7DataGenerator generator = new MyHl7DataGenerator(LayoutFactory.LAYOUT_ID_NAACCR_HL7_2_5_1);
        Assert.assertEquals("Test", generator.generateMessage().getSegment("PID").getField(5).getComponent(1).getValue());
        Assert.assertEquals("Test", generator.generateMessage().getSegment("PID").getField(5).getComponent(1).getValue());
        Assert.assertEquals("Test", generator.generateMessage().getSegment("PID").getField(5).getComponent(1).getValue());
    }

    private class MyHl7DataGenerator extends NaaccrHl7DataGenerator {

        public MyHl7DataGenerator(String layoutId) {
            super(layoutId);

            // let's add a rule that forces a specific last name (in PID-5.1)
            _rules.add(new NaaccrHl7DataGeneratorRule("test", "Test") {
                @Override
                public void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context) {
                    message.getSegment("PID").getField(5).getComponent(1).getSubComponent(1).setValue("Test");
                }
            });
        }
    }
}
