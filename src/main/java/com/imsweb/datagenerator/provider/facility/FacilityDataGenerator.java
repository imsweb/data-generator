/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import com.imsweb.datagenerator.provider.ProviderDataGenerator;
import com.imsweb.datagenerator.provider.facility.rule.NPIRule;
import com.imsweb.datagenerator.provider.facility.rule.NameRule;
import com.imsweb.datagenerator.provider.facility.rule.TypeRule;
import com.imsweb.layout.Layout;

/**
 * A data generator that can be used to create facilities.
 */
public class FacilityDataGenerator extends ProviderDataGenerator {

    /**
     * Constructor
     * @param layout Naaccr HL7 layout to use
     */
    public FacilityDataGenerator(Layout layout) {
        super(layout);

        _rules.add(new NPIRule());
        _rules.add(new NameRule());
        _rules.add(new TypeRule());
    }

}
