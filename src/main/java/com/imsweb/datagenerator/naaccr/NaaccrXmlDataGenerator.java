/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.naaccrxml.NaaccrXmlLayout;
import com.imsweb.naaccrxml.NaaccrFormat;
import com.imsweb.naaccrxml.NaaccrOptions;
import com.imsweb.naaccrxml.NaaccrXmlDictionaryUtils;
import com.imsweb.naaccrxml.PatientXmlWriter;
import com.imsweb.naaccrxml.entity.Item;
import com.imsweb.naaccrxml.entity.NaaccrData;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.dictionary.NaaccrDictionary;

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
        super(layout == null || NaaccrFormat.NAACCR_VERSION_180.compareTo(layout.getNaaccrVersion()) <= 0);

        if (layout == null)
            throw new IllegalArgumentException("A layout is required for creating a NAACCR XML data generator!");
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
        return internalGeneratePatient(numTumors, options, true);
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
        generateFile(file, numTumors, options, null);
    }

    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numTumors number of tumors to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numTumors, NaaccrDataGeneratorOptions options, NaaccrOptions writerOptions) throws IOException {
        // make sure number of tumors is valid
        if (numTumors < 1)
            throw new IllegalArgumentException("Number of tumors must be greater than 0");

        // make sure options are never null
        if (options == null)
            options = new NaaccrDataGeneratorOptions();

        // create a random distribution for the number of tumors, if we have to
        Distribution<Integer> numTumGen = options.getNumTumorsPerPatient() == null ? getNumTumorsPerPatientDistribution() : null;

        // create XML root data
        NaaccrData rootData = new NaaccrData();
        rootData.setBaseDictionaryUri(NaaccrXmlDictionaryUtils.getBaseDictionaryByVersion(_xmlLayout.getLayoutVersion()).getDictionaryUri());
        rootData.setRecordType(_xmlLayout.getRecordType());
        if (options.getRegistryId() != null)
            rootData.addItem(new Item("registryId", options.getRegistryId()));

        // filter user dictionaries (never write the default ones)
        List<NaaccrDictionary> userDictionaries = _xmlLayout.getUserDictionaries().stream()
                .filter(d -> !NaaccrXmlDictionaryUtils.isDefaultUserDictionary(d))
                .collect(Collectors.toList());

        // create file and write to it
        boolean isGZip = file.getName().toLowerCase().endsWith(".gz");
        try (
                OutputStream os = Files.newOutputStream(file.toPath());
                PatientXmlWriter writer = new PatientXmlWriter(new OutputStreamWriter(isGZip ? new GZIPOutputStream(os) : os,
                        StandardCharsets.UTF_8), rootData, writerOptions, userDictionaries)
        ) {
            int numCreatedTumors = 0;
            while (numCreatedTumors < numTumors) {
                int numTumorForThisPatient = Math.min(numTumGen == null ? options.getNumTumorsPerPatient() : numTumGen.getValue(), numTumors - numCreatedTumors);
                _xmlLayout.writeNextPatient(writer, generatePatient(numTumorForThisPatient, options));
                numCreatedTumors += numTumorForThisPatient;
            }
        }
    }

}
