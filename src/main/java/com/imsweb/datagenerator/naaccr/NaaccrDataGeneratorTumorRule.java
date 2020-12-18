package com.imsweb.datagenerator.naaccr;

import java.util.Map;

import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public abstract class NaaccrDataGeneratorTumorRule extends NaaccrDataGeneratorRule {

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    public NaaccrDataGeneratorTumorRule(String id, String name) {
        super(id, name);
    }

    /**
     * Executes the logic of this patient rule.
     * @param tumor current tumor
     * @param patient current patient
     * @param options generator options
     * @param context communication between the rules (must not be null)
     */
    public abstract void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context);
}
