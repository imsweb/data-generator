package com.imsweb.datagenerator.naaccr;

import java.util.Map;

import com.imsweb.naaccrxml.entity.Patient;

public abstract class NaaccrDataGeneratorPatientRule extends NaaccrDataGeneratorRule {

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    protected NaaccrDataGeneratorPatientRule(String id, String name) {
        super(id, name);
    }

    /**
     * Executes the logic of this patient rule.
     * @param patient current patient
     * @param options generator options
     * @param context communication between the rules (must not be null)
     */
    public abstract void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context);
}
