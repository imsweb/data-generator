/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.util.Map;

import com.imsweb.datagenerator.DataGeneratorRule;

public abstract class ProviderDataGeneratorRule implements DataGeneratorRule {

    // the unique ID of this rule
    private String _id;

    // the name of this rule
    private String _name;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    public ProviderDataGeneratorRule(String id, String name) {
        _id = id;
        _name = name;
    }

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    /**
     * Executes the logic of this patient rule.
     * @param provider current record
     * @param options generator options
     */
    public abstract void execute(Map<String, String> provider, ProviderDataGeneratorOptions options);

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
