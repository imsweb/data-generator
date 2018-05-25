/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

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
        List<Patient> patients = readXmlFile(file);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertEquals(8, patients.get(0).getItemValue("dateOfBirth").length());
        Assert.assertTrue(patients.get(0).getAllValidationErrors().isEmpty());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // regular file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-tumors.xml");
        generator.generateFile(file, 10, null);
        patients = readXmlFile(file);
        Assert.assertEquals(10, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 1 record
        file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml.gz");
        generator.generateFile(file, 1, null);
        patients = readXmlFile(file);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 10 records
        file = TestingUtils.createFile("naaccr-data-generator-10-tumors.xml.gz");
        generator.generateFile(file, 10, null);
        patients = readXmlFile(file);
        Assert.assertEquals(10, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // use an option for the number of tumors per patient (3 per; 99 total, so there should be 33 patients)
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setNumTumorsPerPatient(3);
        generator.generateFile(file, 9, options);
        patients = readXmlFile(file);
        Assert.assertEquals(3, patients.size());
        Assert.assertEquals(9, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
    }

    private List<Patient> readXmlFile(File file) throws IOException {
        if (file.getName().endsWith(".gz")) {
            try (InputStream is = new GZIPInputStream(new FileInputStream(file))) {
                return _LAYOUT.readAllPatients(is, "UTF-8", null);
            }
        }
        else
            return _LAYOUT.readAllPatients(file, "UTF-8", null);
    }
}
