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
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        if (StringUtils.isBlank(options.getState()))
            options.setState("MD");

        // get facility.
        FacilityFrequencyDto freqFacility = DistributionUtils.getFacility(options.getState());

        FacilityFrequencyDto facility = new FacilityFrequencyDto();
        facility.setNpi(freqFacility.getNpi());
        facility.setName(freqFacility.getName());
        facility.setAddressFirstLine(freqFacility.getAddressFirstLine());
        facility.setAddressSecondLine(freqFacility.getAddressSecondLine());
        facility.setAddressCity(freqFacility.getAddressCity());
        facility.setAddressState(freqFacility.getAddressState());
        facility.setAddressPostalCode(freqFacility.getAddressPostalCode());
        facility.setAddressTelephone(freqFacility.getAddressTelephone());
        facility.setSpecialty01(freqFacility.getSpecialty(0));
        facility.setSpecialty02(freqFacility.getSpecialty(1));
        facility.setSpecialty03(freqFacility.getSpecialty(2));

        return facility;
    }

    /**
     * Generates a list of facilities.
     * <br/><br/>
     * @param numProviders the number of facilities to create
     * @param options options
     * @return list of generated facilities
     */
    public List<FacilityFrequencyDto> generateFacilities(int numProviders, ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<FacilityFrequencyDto> facilities = new ArrayList<>();
        for (int i = 0; i < numProviders; i++) {
            FacilityFrequencyDto facility = generateFacility(options);
            if (!facilities.contains(facility))
                facilities.add(facility);
        }

        return facilities;
    }

}
