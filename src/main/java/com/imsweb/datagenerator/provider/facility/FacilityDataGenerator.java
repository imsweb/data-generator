/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import com.imsweb.datagenerator.provider.ProviderDataGenerator;
import com.imsweb.datagenerator.provider.facility.rule.NameRule;

/**
 * A data generator that can be used to create facilities.
 */
public class FacilityDataGenerator extends ProviderDataGenerator {

    /**
     * Constructor
     */
    public FacilityDataGenerator() {
        super();

        _rules.add(new NameRule());
    }

}
