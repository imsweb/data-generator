/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;

/**
 * A data generator that can be used to create facilities.
 */
public class FacilityDataGenerator implements DataGenerator {

    @Override
    public String getId() {
        return "FacilityDataGenerator";
    }

    /**
     * Generates a single facility.
     * <br/><br/>
     * @param options options
     * @return generated FacilityFrequencyDto
     */
    private FacilityFrequencyDto generateFacility(ProviderDataGeneratorOptions options) {
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        if (StringUtils.isBlank(options.getState()))
            options.setState("MD");

        return DistributionUtils.getFacility(options.getState());
    }

    /**
     * Generates a list of facilities.
     * <br/><br/>
     * @param numProviders the number of facilities to create
     * @param options options
     * @return list of generated facilities
     */
    public List<FacilityFrequencyDto> generateFacilities(int numProviders, ProviderDataGeneratorOptions options) {
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<FacilityFrequencyDto> facilities = new ArrayList<>();
        for (int i = 0; i < numProviders; i++)
            facilities.add(generateFacility(options));

        return facilities;
    }

}
