/*
 * Copyright (C) 2023 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.Map;

import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public abstract class NaaccrDataGeneratorTumorTextRule extends NaaccrDataGeneratorTumorRule {

    protected NaaccrDataGeneratorTumorTextRule(String id, String name) {
        super(id, name);
    }

    public abstract String[] getNAACCRTextFields();

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        for (String property : getNAACCRTextFields())
            setValue(tumor, property, RandomUtils.getRandomText());
    }
}
