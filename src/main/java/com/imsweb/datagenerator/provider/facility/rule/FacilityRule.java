/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;

public class FacilityRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "facility-name";

    /**
     * Constructor.
     */
    public FacilityRule() {
        super(ID, "Facility Name");
    }

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {
        if (options == null || options.getState() == null)
            return;

        // get facility.
        FacilityFrequencyDto dto = DistributionUtils.getFacility(options.getState());
        provider.put("npi", dto.getNpi());
        provider.put("name", dto.getName());
        provider.put("addressFirstLine", dto.getAddressFirstLine());
        provider.put("addressSecondLine", dto.getAddressSecondLine());
        provider.put("addressCity", dto.getAddressCity());
        provider.put("addressState", dto.getAddressState());
        provider.put("addressPostalCode", dto.getAddressPostalCode());
        provider.put("addressTelephone", dto.getAddressTelephone());
        provider.put("specialty1", dto.getSpecialty(0));
        provider.put("specialty2", dto.getSpecialty(1));
        provider.put("specialty3", dto.getSpecialty(2));
    }

}
