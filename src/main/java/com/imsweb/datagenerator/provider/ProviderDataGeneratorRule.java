/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import java.util.Collections;
import java.util.List;
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
     * Returns the required properties for this rule. If one of those properties doesn't have a value, the rule will be skipped.
     */
    public List<String> getRequiredProperties() {
        return Collections.emptyList();
    }

    /**
     * Executes the logic of this patient rule.
     * @param provider current record
     * @param options generator options
     */
    public abstract void execute(Map<String, String> provider, ProviderDataGeneratorOptions options);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof com.imsweb.datagenerator.provider.ProviderDataGeneratorRule)) return false;

        com.imsweb.datagenerator.provider.ProviderDataGeneratorRule that = (com.imsweb.datagenerator.provider.ProviderDataGeneratorRule)o;

        return _id.equals(that._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
