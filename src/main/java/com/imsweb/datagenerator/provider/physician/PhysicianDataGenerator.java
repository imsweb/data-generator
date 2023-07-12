/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.PhysicianFrequencyDto;

/**
 * A data generator that can be used to create physicians.
 */
public class PhysicianDataGenerator implements DataGenerator {

    @Override
    public String getId() {
        return "FacilityDataGenerator";
    }

    /**
     * Generates a single physician.
     * <br/><br/>
     * @param options options
     * @return generated PhysicianFrequencyDto
     */
    private PhysicianFrequencyDto generatePhysician(ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        if (StringUtils.isBlank(options.getState()))
            options.setState("MD");

        return DistributionUtils.getPhysician(options.getState());
    }

    /**
     * Generates a list of physicians.
     * <br/><br/>
     * @param numProviders the number of physicians to create
     * @param options options
     * @return list of generated physicians
     */
    public List<PhysicianFrequencyDto> generatePhysicians(int numProviders, ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<PhysicianFrequencyDto> physicians = new ArrayList<>();
        for (int i = 0; i < numProviders; i++) {
            PhysicianFrequencyDto physician = generatePhysician(options);
            if (!physicians.contains(physician))
                physicians.add(physician);
        }

        return physicians;
    }

}
