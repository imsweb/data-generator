/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider;

import com.imsweb.datagenerator.utils.DistributionUtils;

public class ProviderDataGeneratorOptions {

    // the state abbreviation (upper-cased) to use to generate the address information (no address info will be generated if left null)
    private String _state;

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        if (state != null && !DistributionUtils.getAllStates().contains(state.toUpperCase()))
            throw new IllegalArgumentException("State '" + state + "' is not a valid US state");
        _state = state;
    }
}
