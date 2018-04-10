/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician;

import com.imsweb.datagenerator.provider.ProviderDataGenerator;
import com.imsweb.datagenerator.provider.physician.rule.PhysicianRule;

/**
 * A data generator that can be used to create physicians.
 */
public class PhysicianDataGenerator extends ProviderDataGenerator {

    /**
     * Constructor
     */
    public PhysicianDataGenerator() {
        super();

        _rules.add(new PhysicianRule());
    }

    @Override
    public String getId() {
        return "PhysicianDataGenerator";
    }

}
