/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician;

import com.imsweb.datagenerator.provider.ProviderDataGenerator;
import com.imsweb.datagenerator.provider.physician.rule.NPIRule;
import com.imsweb.datagenerator.provider.physician.rule.NameRule;
import com.imsweb.datagenerator.provider.physician.rule.SpecialtyRule;
import com.imsweb.layout.Layout;

/**
 * A data generator that can be used to create physicians.
 */
public class PhysicianDataGenerator extends ProviderDataGenerator {

    /**
     * Constructor
     * @param layout Naaccr HL7 layout to use
     */
    public PhysicianDataGenerator(Layout layout) {
        super(layout);

        _rules.add(new NPIRule());
        _rules.add(new NameRule());
        _rules.add(new SpecialtyRule());
    }

}
