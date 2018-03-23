/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.layout.Layout;
import com.imsweb.layout.LayoutFactory;

/**
 * A data generator can be used to create fake NAACCR HL7 data files.
 */
public class FacilityDataGenerator implements DataGenerator {

    // the layout used for this generator (the layout defines the variables that can be used in the rules)
    protected Layout _layout;

    // list of rules to be executed
    protected List<FacilityDataGeneratorRule> _rules;

    /**
     * Constructor
     * @param layoutId NAACCR HL7 layout ID to use
     */
    public FacilityDataGenerator(String layoutId) {
        this(LayoutFactory.getLayout(layoutId));
    }

    /**
     * Constructor
     * @param layout Naaccr HL7 layout to use
     */
    public FacilityDataGenerator(Layout layout) {
        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR HL7 data generator!");
        if (!(layout instanceof Layout))
            throw new RuntimeException("A NAACCR HL7 layout is required for creating a NAACCR HL7 data generator!");
        _layout = (Layout)layout;
        _rules = new ArrayList<>();

        /*
        _rules.add(new ControlSegmentRule());
        _rules.add(new PatientIdentifierSegmentRule());
        _rules.add(new NextOfKinSegmentRule());
        _rules.add(new PatientVisitSegmentRule());
        _rules.add(new CommonOrderSegmentRule());
        _rules.add(new ObservationRequestSegmentRule());
        _rules.add(new ObservationSegmentRule());
        */
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    /**
     * Returns all the rules from this generator.
     */
    public List<FacilityDataGeneratorRule> getRules() {
        return _rules;
    }

    /**
     * Generates a single patient with a requested number of tumors.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param options options
     * @return generated patient as a list of tumor maps, never null
     */
    public Map<String, String> generateProvider(FacilityDataGeneratorOptions options) {

        // make sure options are never null
        if (options == null)
            options = new FacilityDataGeneratorOptions();

        // execute the patient rules once; we will copy the resulting values in each tumor
        Map<String, String> provider = new HashMap<>();
        for (FacilityDataGeneratorRule rule : _rules)
            if (allPropertiesHaveValue(provider, rule.getRequiredProperties()))
                rule.execute(provider, options);

        return provider;
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

    /**
     * Generates a requested number of messages and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     */
    public void generateFile(File file) throws IOException {
        generateFile(file, null);
    }

    /**
     * Generates a requested number of tumors and saves them in the specified file.
     * <br/><br/>
     * Every patient field will have the same value on every generated tumor.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, FacilityDataGeneratorOptions options) throws IOException {

        // make sure options are never null
        if (options == null)
            options = new FacilityDataGeneratorOptions();

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            Map<String, String> provider = generateProvider(options);
            //_layout.writeRecord(writer, provider);

        }
    }

}
