/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.layout.Layout;
import com.imsweb.layout.LayoutFactory;

public abstract class ProviderDataGenerator implements DataGenerator {

    private Layout _layout;

    // list of rules to be executed
    protected List<ProviderDataGeneratorRule> _rules;

    public ProviderDataGenerator(String layoutId) {
        this(LayoutFactory.getLayout(layoutId));
    }

    public ProviderDataGenerator(Layout layout) {
        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR HL7 data generator!");
        //if (!(layout instanceof Layout))
        //    throw new RuntimeException("A NAACCR HL7 layout is required for creating a NAACCR HL7 data generator!");
        _layout = layout;
        _rules = new ArrayList<>();
    }

    @Override
    public String getId() {
        return "0";
    }

    /**
     * Returns all the provider rules from this generator.
     */
    public List<ProviderDataGeneratorRule> getRules() {
        return _rules;
    }

    /**
     * Generates a single provider.
     * <br/><br/>
     * @param options options
     * @return generated provider
     */
    private Map<String, String> generateProvider(ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        // execute the facility rules once
        Map<String, String> provider = new HashMap<>();
        for (ProviderDataGeneratorRule rule : _rules)
            if (allPropertiesHaveValue(provider, rule.getRequiredProperties()))
                rule.execute(provider, options);

        return provider;
    }

    /**
     * Generates a list of providers.
     * <br/><br/>
     * @param numProviders the number of providers to create
     * @param options options
     * @return list of generated providers
     */
    private List<Map<String, String>> generateProviders(int numProviders, ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<Map<String, String>> providers = new ArrayList<>();
        for (int i = 0; i < numProviders; i++) {
            Map<String, String> facility = generateProvider(options);
            providers.add(facility);
        }

        return providers;
    }

    /**
     * Returns true if all the requested properties have a non-blank value on the provided record.
     */
    private boolean allPropertiesHaveValue(Map<String, String> record, List<String> properties) {
        for (String property : properties) {
            String val = record.get(property);
            if (val == null || val.trim().isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Generates a requested number of providers and saves them in the specified file.
     * <br/><br/>
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numProviders number of providers to create
     */
    public void generateFile(File file, int numProviders) throws IOException {
        generateFile(file, numProviders, null);
    }

    /**
     * Generates a requested number of providers and saves them in the specified file.
     * <br/><br/>
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numProviders number of providers to create
     * @param options options that will be provided to every rules.
     */
    private void generateFile(File file, int numProviders, ProviderDataGeneratorOptions options) throws IOException {

        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            List<Map<String, String>> providerList = generateProviders(numProviders, options);
            //for (int i = 0; i < providerList.size(); i++) {
            //    _layout.writeRecord(writer, providerList[i]);
            //}
        }
    }

}
