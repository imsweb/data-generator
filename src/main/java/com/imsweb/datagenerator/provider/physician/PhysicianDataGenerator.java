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

    /**
     * Constructor
     */
    public PhysicianDataGenerator() {}

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
    private PhysicianDto generatePhysician(ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        if (StringUtils.isBlank(options.getState()))
            options.setState("MD");

        // get physician.
        PhysicianFrequencyDto freqPhysician = DistributionUtils.getPhysician(options.getState());

        PhysicianDto physician = new PhysicianDto();
        physician.setNpi(freqPhysician.getNpi());
        physician.setLastName(freqPhysician.getLastName());
        physician.setFirstName(freqPhysician.getFirstName());
        physician.setMiddleName(freqPhysician.getMiddleName());
        physician.setNamePrefix(freqPhysician.getNamePrefix());
        physician.setNameSuffix(freqPhysician.getNameSuffix());
        physician.setCredentials(freqPhysician.getCredentials());
        physician.setAddressFirstLine(freqPhysician.getAddressFirstLine());
        physician.setAddressSecondLine(freqPhysician.getAddressSecondLine());
        physician.setAddressCity(freqPhysician.getAddressCity());
        physician.setAddressState(freqPhysician.getAddressState());
        physician.setAddressPostalCode(freqPhysician.getAddressPostalCode());
        physician.setAddressTelephone(freqPhysician.getAddressTelephone());
        physician.setSpecialty01(freqPhysician.getSpecialty(0));
        physician.setSpecialty02(freqPhysician.getSpecialty(1));
        physician.setSpecialty03(freqPhysician.getSpecialty(2));

        return physician;
    }

    /**
     * Generates a list of physicians.
     * <br/><br/>
     * @param numProviders the number of physicians to create
     * @param options options
     * @return list of generated physicians
     */
    public List<PhysicianDto> generatePhysicians(int numProviders, ProviderDataGeneratorOptions options) {
        // make sure options are never null
        if (options == null)
            options = new ProviderDataGeneratorOptions();

        List<PhysicianDto> physicians = new ArrayList<>();
        for (int i = 0; i < numProviders; i++) {
            PhysicianDto physician = generatePhysician(options);
            if (!physicians.contains(physician))
                physicians.add(physician);
        }

        return physicians;
    }

}
