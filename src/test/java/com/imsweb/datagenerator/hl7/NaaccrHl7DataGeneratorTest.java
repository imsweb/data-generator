/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.hl7.NaaccrHl7Layout;

public class NaaccrHl7DataGeneratorTest {

    @Test
    public void testGenerator() throws IOException {

        NaaccrHl7Layout layout = (NaaccrHl7Layout)LayoutFactory.getLayout(LayoutFactory.LAYOUT_ID_NAACCR_HL7_2_5_1);

        NaaccrHl7DataGenerator generator = new NaaccrHl7DataGenerator(layout);

        Path buildDir = Paths.get("build");

        NaaccrHl7DataGeneratorOptions options = new NaaccrHl7DataGeneratorOptions();
        options.setGenerateSiteAndMorphology(true);
        options.setState("MD");

        generator.generateFile(buildDir.resolve("naaccr-hl7-test.txt").toFile(), 3, options);
    }

}
