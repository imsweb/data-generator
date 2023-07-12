package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorPatientRule;
import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.RandomUtils;
import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;
import com.imsweb.naaccrxml.entity.Patient;

public class AddressCurrentRule extends NaaccrDataGeneratorPatientRule {

    // unique identifier for this rule
    public static final String ID = "address-current";

    /**
     * Constructor.
     */
    public AddressCurrentRule() {
        super(ID, "Current address");
    }

    @Override
    public void execute(Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (options == null || options.getState() == null)
            return;

        // get random zip code and with it city and state
        CityFrequencyDto dto = DistributionUtils.getCity(options.getState());
        setValue(patient, "addrCurrentCity", dto.getCity());
        setValue(patient, "addrCurrentPostalCode", dto.getZip());
        setValue(patient, "addrCurrentState", dto.getState());
        setValue(patient, "countyCurrent", "999");
        setValue(patient, "addrCurrentCountry", "USA");

        // put random street name with house number (1 - 9999)
        String name = DistributionUtils.getStreetName();
        String suffix = DistributionUtils.getStreetSuffix();
        setValue(patient, "addrCurrentNoStreet", (RandomUtils.nextInt(9998) + 1) + " " + name + " " + suffix);
    }
}
