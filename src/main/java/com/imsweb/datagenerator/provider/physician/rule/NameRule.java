/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.physician.rule;

import java.util.Map;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;
import com.imsweb.datagenerator.provider.ProviderDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.PhysicianFrequencyDto;

public class NameRule extends ProviderDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "physician-name";

    /**
     * Constructor.
     */
    public NameRule() {
        super(ID, "Physician Name");
    }

    @Override
    public void execute(Map<String, String> provider, ProviderDataGeneratorOptions options) {
        if (options == null || options.getState() == null)
            return;

        // get facility.
        PhysicianFrequencyDto dto = DistributionUtils.getPhysician(options.getState());
        provider.put("npi", dto.getNpi());
        provider.put("nameLast", dto.getLastName());
        provider.put("nameFirst", dto.getFirstName());
        provider.put("nameMiddle", dto.getMiddleName());
        provider.put("namePrefix", dto.getNamePrefix());
        provider.put("nameSuffix", dto.getNameSuffix());
        provider.put("credentials", dto.getCredentials());
        provider.put("addressFirstLine", dto.getAddressFirstLine());
        provider.put("addressSecondLine", dto.getAddressSecondLine());
        provider.put("addressCity", dto.getAddressCity());
        provider.put("addressState", dto.getAddressState());
        provider.put("addressPostalCode", dto.getAddressPostalCode());
        provider.put("addressTelephone", dto.getAddressTelephone());
        for (int i = 0; i < 15; i++) {
            String thisSpecialty = dto.getSpecialty(i);
            if ((thisSpecialty != null) && (!thisSpecialty.trim().equals("")))
                provider.put("specialty" + (i + 1), thisSpecialty);
        }
    }
}
