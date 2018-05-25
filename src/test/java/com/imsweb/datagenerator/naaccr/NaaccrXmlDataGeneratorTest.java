/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import testing.TestingUtils;

import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.naaccrxml.NaaccrXmlLayout;
import com.imsweb.naaccrxml.entity.Patient;

public class NaaccrXmlDataGeneratorTest {

    // we are going to use this layout
    private static final NaaccrXmlLayout _LAYOUT = (NaaccrXmlLayout)LayoutFactory.getLayout(LayoutFactory.LAYOUT_ID_NAACCR_XML_18_ABSTRACT);

    @Test
    public void testGenerate() throws IOException {
        NaaccrXmlDataGenerator generator = new NaaccrXmlDataGenerator(_LAYOUT);

        // regular file, 1 tumor
        File file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml");
        generator.generateFile(file, 1, null);
        List<Patient> patients = _LAYOUT.readAllPatients(file, "UTF-8", null);
        Assert.assertEquals(1, patients.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());
/*
        // regular file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 1 record
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt.gz");
        generator.generateFile(file, 1, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(1, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-rec.txt.gz");
        generator.generateFile(file, 10, null);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(10, lines.size());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // use an option for the number of tumors per patient (3 per; 99 total, so there should be 33 patients)
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setNumTumorsPerPatient(3);
        generator.generateFile(file, 99, options);
        lines = TestingUtils.readFile(file);
        Assert.assertEquals(99, lines.size());
        FixedColumnsField patientIdNumberField = _LAYOUT.getFieldByName("patientIdNumber");
        Assert.assertNotNull(patientIdNumberField);
        Set<String> patientIdNumberValues = new HashSet<>();
        for (String line : lines)
            patientIdNumberValues.add(line.substring(patientIdNumberField.getStart() - 1, patientIdNumberField.getEnd()));
        Assert.assertEquals(33, patientIdNumberValues.size());
        */
    }
}
