/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;

public class NameRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "facility-name";

    /**
     * Constructor.
     */
    public NameRule() {
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
        for (int i = 0; i < 15; i++) {
            if (dto.getSpecialty(i) != null)
                if (dto.getSpecialty(i).trim().equals(""))
                    provider.put("specialty" + (i + 1), dto.getSpecialty(i));
        }
    }

}
