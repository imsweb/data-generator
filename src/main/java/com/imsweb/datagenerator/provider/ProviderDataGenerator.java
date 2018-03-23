/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import com.imsweb.datagenerator.DataGenerator;

public abstract class ProviderDataGenerator implements DataGenerator {

    @Override
    public String getId() {
        return "0";
    }

}
