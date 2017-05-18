/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.hl7;

import com.imsweb.datagenerator.utils.DistributionUtils;

public class NaaccrHl7DataGeneratorOptions {

    // the state abbreviation (upper-cased) to use to generate the address information (no address info will be generated if left null)
    protected String _state;

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        if (state != null && !DistributionUtils.getAllStates().contains(state.toUpperCase()))
            throw new IllegalArgumentException("State '" + state + "' is not a valid US state");
        _state = state;
    }

}
