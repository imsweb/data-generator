package com.imsweb.datagenerator.naaccr;

import java.util.Map;

import com.imsweb.naaccrxml.entity.NaaccrData;

public abstract class NaaccrDataGeneratorNaaccrDataRule extends NaaccrDataGeneratorRule {

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    public NaaccrDataGeneratorNaaccrDataRule(String id, String name) {
        super(id, name);
    }

    /**
     * Executes the logic of this patient rule.
     * @param naaccrData current naaccrData
     * @param options generator options
     * @param context communication between the rules (must not be null)
     */
    public abstract void execute(NaaccrData naaccrData, NaaccrDataGeneratorOptions options, Map<String, Object> context);
}
