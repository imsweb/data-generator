/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.naaccrxml.NaaccrXmlLayout;
import com.imsweb.naaccrxml.NaaccrXmlDictionaryUtils;
import com.imsweb.naaccrxml.NaaccrXmlUtils;
import com.imsweb.naaccrxml.PatientXmlWriter;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.NaaccrData;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;
import com.imsweb.naaccrxml.entity.dictionary.NaaccrDictionaryItem;

/**
 * This NAACCR Data Generators uses (and requires) a NAACCR XML layout.
 */
@SuppressWarnings("unused")
public class NaaccrXmlDataGenerator extends NaaccrDataGenerator {

    // NAACCR XML layout to use for this generator
    private final NaaccrXmlLayout _xmlLayout;

    /**
     * Constructor
     * @param layoutId NAACCR layout ID to use for this generator
     */
    public NaaccrXmlDataGenerator(String layoutId) {
        this(LayoutFactory.getNaaccrXmlLayout(layoutId));
    }

    /**
     * Constructor
     * @param layout NAACCR layout to use for this generator
     */
    public NaaccrXmlDataGenerator(NaaccrXmlLayout layout) {
        super();

        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR XML data generator!");
        _xmlLayout = layout;
    }

    @Override
    public String getId() {
        return _xmlLayout.getLayoutId();
    }

    @Override
    protected boolean isInvalidValidField(String name) {
        return _xmlLayout.getFieldByName(name) == null;
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param numTumors number of tumors to generate
     * @return generated patient as a list of tumor maps, never null
     */
    public Patient generatePatient(int numTumors) {
        return generatePatient(numTumors, null);
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param numTumors number of tumors to generate
     * @param options options
     * @return generated patient as a list of tumor maps, never null
     */
    public Patient generatePatient(int numTumors, NaaccrDataGeneratorOptions options) {
        return createPatient(generatePatientAsListOfMaps(numTumors, options));
    }

    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numTumors number of tumors to generate, must be greater than 0
     */
    public void generateFile(File file, int numTumors) throws IOException {
        generateFile(file, numTumors, null);
    }

    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numTumors number of tumors to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numTumors, NaaccrDataGeneratorOptions options) throws IOException {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // create a random distribution for the number of tumors, if we have to
        Distribution<Integer> numTumGen = options.getNumTumorsPerPatient() == null ? getNumTumorsPerPatientDistribution() : null;

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        NaaccrData rootData = new NaaccrData();
        rootData.setBaseDictionaryUri(NaaccrXmlDictionaryUtils.getBaseDictionaryByVersion(_xmlLayout.getLayoutVersion()).getDictionaryUri());
        rootData.setRecordType(_xmlLayout.getRecordType());
        if (options.getRegistryId() != null)
            rootData.addItem(new Item("registryId", options.getRegistryId()));

        try (PatientXmlWriter writer = new PatientXmlWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), rootData)) {
            int numCreatedTumors = 0;
            while (numCreatedTumors < numTumors) {
                int numTumorForThisPatient = Math.min(numTumGen == null ? options.getNumTumorsPerPatient() : numTumGen.getValue(), numTumors - numCreatedTumors);
                _xmlLayout.writeNextPatient(writer, generatePatient(numTumorForThisPatient, options));
                numCreatedTumors += numTumorForThisPatient;
            }
        }
    }

    /**
     * Returns the distribution to use for generating the number of tumors for a specific patient.
     */
    public Distribution<Integer> getNumTumorsPerPatientDistribution() {
        Map<Integer, Double> frequencies = new HashMap<>();
        frequencies.put(1, 95.0);
        frequencies.put(2, 4.0);
        frequencies.put(3, 1.0);
        return Distribution.of(frequencies);
    }

    /**
     * Returns true if all the requested properties have a non-blank value on the provided record.
     */
    protected boolean allPropertiesHaveValue(Map<String, String> record, List<String> properties) {
        for (String property : properties) {
            String val = record.get(property);
            if (val == null || val.trim().isEmpty())
                return false;
        }
        return true;
    }

    // at some point the NAACCR generate should natively generate Patients/Tumors and we should stop using maps...
    private Patient createPatient(List<Map<String, String>> records) {
        Patient patient = new Patient();

        // deal with the three-part dates
        for (Map<String, String> record : records) {
            Set<String> toRemove = new HashSet<>();
            Map<String, StringBuilder> toAdd = new HashMap<>();
            for (Entry<String, String> entry : record.entrySet()) {
                if (entry.getKey().endsWith("Year")) {
                    toAdd.computeIfAbsent(entry.getKey().replace("Year", ""), k -> new StringBuilder("        ")).replace(0, 4, entry.getValue());
                    toRemove.add(entry.getKey());
                }
                else if (entry.getKey().endsWith("Month")) {
                    toAdd.computeIfAbsent(entry.getKey().replace("Month", ""), k -> new StringBuilder("        ")).replace(4, 6, StringUtils.leftPad(entry.getValue(), 2, "0"));
                    toRemove.add(entry.getKey());
                }
                else if (entry.getKey().endsWith("Day")) {
                    toAdd.computeIfAbsent(entry.getKey().replace("Day", ""), k -> new StringBuilder("        ")).replace(6, 8, StringUtils.leftPad(entry.getValue(), 2, "0"));
                    toRemove.add(entry.getKey());
                }
            }
            toRemove.forEach(record::remove);
            toAdd.forEach((k, v) -> record.put(k, v.toString()));
        }

        // populate patient items from the first record (shouldn't matter which one)
        for (Entry<String, String> entry : records.get(0).entrySet()) {
            String naaccrId = entry.getKey();
            NaaccrDictionaryItem item = _xmlLayout.getBaseDictionary().getItemByNaaccrId(naaccrId);
            if (item != null && NaaccrXmlUtils.NAACCR_XML_TAG_PATIENT.equals(item.getParentXmlElement()))
                patient.addItem(new Item(naaccrId, entry.getValue()));
        }

        // populate tumors
        for (Map<String, String> record : records) {
            Tumor tumor = new Tumor();
            for (Entry<String, String> entry : record.entrySet()) {
                String naaccrId = entry.getKey();
                NaaccrDictionaryItem item = _xmlLayout.getBaseDictionary().getItemByNaaccrId(naaccrId);
                if (item != null && NaaccrXmlUtils.NAACCR_XML_TAG_TUMOR.equals(item.getParentXmlElement()))
                    tumor.addItem(new Item(naaccrId, entry.getValue()));
            }
            patient.addTumor(tumor);
        }

        return patient;
    }
}
