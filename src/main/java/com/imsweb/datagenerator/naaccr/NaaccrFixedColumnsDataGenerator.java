/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.RecordLayoutOptions;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

/**
 * This NAACCR Data Generators uses (and requires) a NAACCR fixed-columns layout.
 */
@SuppressWarnings("unused")
public class NaaccrFixedColumnsDataGenerator extends NaaccrDataGenerator {

    // NAACCR layout to use for this generator
    private final NaaccrLayout _layout;

    /**
     * Constructor
     * @param layoutId NAACCR layout ID to use for this generator
     */
    public NaaccrFixedColumnsDataGenerator(String layoutId) {
        this(LayoutFactory.getNaaccrFixedColumnsLayout(layoutId));
    }

    /**
     * Constructor
     * @param layout NAACCR layout to use for this generator
     */
    public NaaccrFixedColumnsDataGenerator(NaaccrLayout layout) {
        super(true);

        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR data generator!");
        _layout = layout;
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    @Override
    protected boolean isInvalidValidField(String name) {
        return _layout.getFieldByName(name) == null;
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param numTumors number of tumors to generate
     * @return generated patient as a list of tumor maps, never null
     */
    public List<Map<String, String>> generatePatient(int numTumors) {
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
    public List<Map<String, String>> generatePatient(int numTumors, NaaccrDataGeneratorOptions options) {
        Patient patient = internalGeneratePatient(numTumors, options, false);

        List<Map<String, String>> records = new ArrayList<>();

        for (Tumor tumor : patient.getTumors()) {
            Map<String, String> rec = new HashMap<>();

            if (options != null && options.getRegistryId() != null)
                rec.put("registryId", options.getRegistryId());

            patient.getItems().forEach(i -> rec.put(i.getNaaccrId(), i.getValue()));
            tumor.getItems().forEach(i -> rec.put(i.getNaaccrId(), i.getValue()));

            records.add(rec);
        }

        return records;
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

        // create the file and write to it
        try (OutputStream os = createOutputStream(file);
             Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {

            RecordLayoutOptions layoutOptions = new RecordLayoutOptions();
            layoutOptions.setValueTooLongHandling(RecordLayoutOptions.VAL_TOO_LONG_NULLIFY);

            int numCreatedTumors = 0;
            while (numCreatedTumors < numTumors) {
                int numTumorForThisPatient = numTumGen == null ? options.getNumTumorsPerPatient() : numTumGen.getValue();
                // never create more tumors than requested, so we use a min() call
                for (Map<String, String> tumor : generatePatient(Math.min(numTumorForThisPatient, numTumors - numCreatedTumors), options)) {
                    _layout.writeRecord(writer, tumor, layoutOptions);
                    numCreatedTumors++;
                }
            }
        }
    }
}
