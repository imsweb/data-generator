/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.imsweb.layout.hl7.NaaccrHl7Layout;

public class NaaccrHl7DataGeneratorTest {

    @Test
    public void testGenerator() throws IOException {

        NaaccrHl7Layout layout = new NaaccrHl7Layout(); // TODO this will need to change...

        NaaccrHl7DataGenerator generator = new NaaccrHl7DataGenerator(layout);

        Path buildDir = Paths.get("build");

        generator.generateFile(buildDir.resolve("naaccr-hl7-test.txt").toFile(), 3, null);
    }

}
