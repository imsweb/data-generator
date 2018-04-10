/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.DataGenerator;

public abstract class ProviderDataGenerator implements DataGenerator {

    // list of rules to be executed
    protected List<ProviderDataGeneratorRule> _rules;

    public ProviderDataGenerator() {
        _rules = new ArrayList<>();
    }

    @Override
    public abstract String getId();

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
    public Map<String, String> generateProvider(ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        if ((options.getState() == null) || (options.getState().equals("")))
            options.setState("MD");

        // execute the facility rules once
        Map<String, String> provider = new HashMap<>();
        for (ProviderDataGeneratorRule rule : _rules)
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
    public List<Map<String, String>> generateProviders(int numProviders, ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<Map<String, String>> providers = new ArrayList<>();
        for (int i = 0; i < numProviders; i++) {
            Map<String, String> provider = generateProvider(options);
            if (!providers.contains(provider))
                providers.add(provider);
        }

        return providers;
    }

}
