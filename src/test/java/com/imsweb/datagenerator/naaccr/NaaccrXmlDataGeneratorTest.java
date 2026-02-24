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
import com.imsweb.naaccrxml.NaaccrXmlUtils;
import com.imsweb.naaccrxml.entity.Patient;

public class NaaccrXmlDataGeneratorTest {

    @Test
    public void testGenerateN26() throws IOException {
        NaaccrXmlLayout layout = LayoutFactory.getNaaccrXmlLayout(LayoutFactory.LAYOUT_ID_NAACCR_XML_26_ABSTRACT);

        NaaccrXmlDataGenerator generator = new NaaccrXmlDataGenerator(layout);

        // regular file, 1 tumor
        File file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml");
        generator.generateFile(file, 1, null);
        List<Patient> patients = readXmlFile(file, layout);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertEquals(8, patients.getFirst().getItemValue("dateOfBirth").length());
        Assert.assertNotNull(patients.getFirst().getItemValue("sexAssignedAtBirth"));
        Assert.assertTrue(patients.getFirst().getAllValidationErrors().isEmpty());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());
        Assert.assertTrue(NaaccrXmlUtils.readXmlFile(file, null, null, null).getUserDictionaryUri().isEmpty());

        // regular file, 10 tumors
        file = TestingUtils.createFile("naaccr-data-generator-10-tumors.xml");
        generator.generateFile(file, 10, null);
        patients = readXmlFile(file, layout);
        Assert.assertEquals(10, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 1 tumor
        file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml.gz");
        generator.generateFile(file, 1, null);
        patients = readXmlFile(file, layout);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // compressed file, 10 tumors
        file = TestingUtils.createFile("naaccr-data-generator-10-tumors.xml.gz");
        generator.generateFile(file, 10, null);
        patients = readXmlFile(file, layout);
        Assert.assertEquals(10, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());

        // use an option for the number of tumors per patient (3 per; 99 total, so there should be 33 patients)
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setNumTumorsPerPatient(3);
        generator.generateFile(file, 9, options);
        patients = readXmlFile(file, layout);
        Assert.assertEquals(3, patients.size());
        Assert.assertEquals(9, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
    }

    @Test
    public void testGenerateN25() throws IOException {
        NaaccrXmlLayout layout = LayoutFactory.getNaaccrXmlLayout(LayoutFactory.LAYOUT_ID_NAACCR_XML_25_ABSTRACT);

        NaaccrXmlDataGenerator generator = new NaaccrXmlDataGenerator(layout);

        // regular file, 1 tumor
        File file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml");
        generator.generateFile(file, 1, null);
        List<Patient> patients = readXmlFile(file, layout);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertEquals(8, patients.getFirst().getItemValue("dateOfBirth").length());
        Assert.assertNotNull(patients.getFirst().getItemValue("sex"));
        Assert.assertTrue(patients.getFirst().getAllValidationErrors().isEmpty());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());
        Assert.assertTrue(NaaccrXmlUtils.readXmlFile(file, null, null, null).getUserDictionaryUri().isEmpty());
    }

    @Test
    public void testGenerateN24() throws IOException {
        NaaccrXmlLayout layout = LayoutFactory.getNaaccrXmlLayout(LayoutFactory.LAYOUT_ID_NAACCR_XML_24_ABSTRACT);

        NaaccrXmlDataGenerator generator = new NaaccrXmlDataGenerator(layout);

        // regular file, 1 tumor
        File file = TestingUtils.createFile("naaccr-data-generator-1-tumor.xml");
        generator.generateFile(file, 1, null);
        List<Patient> patients = readXmlFile(file, layout);
        Assert.assertEquals(1, patients.stream().map(Patient::getTumors).map(List::size).mapToInt(Integer::intValue).sum());
        Assert.assertEquals(8, patients.getFirst().getItemValue("dateOfBirth").length());
        Assert.assertNotNull(patients.getFirst().getItemValue("sex"));
        Assert.assertTrue(patients.getFirst().getAllValidationErrors().isEmpty());
        Assert.assertFalse(LayoutFactory.discoverFormat(file).isEmpty());
        Assert.assertTrue(NaaccrXmlUtils.readXmlFile(file, null, null, null).getUserDictionaryUri().isEmpty());
    }

    private List<Patient> readXmlFile(File file, NaaccrXmlLayout layout) throws IOException {
        if (file.getName().endsWith(".gz")) {
            try (InputStream is = new GZIPInputStream(new FileInputStream(file))) {
                return layout.readAllPatients(is, "UTF-8", null);
            }
        }
        else
            return layout.readAllPatients(file, "UTF-8", null);
    }
}
