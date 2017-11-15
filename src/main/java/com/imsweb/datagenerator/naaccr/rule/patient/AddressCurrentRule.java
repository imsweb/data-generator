package com.imsweb.datagenerator.naaccr.rule.patient;

import com.imsweb.datagenerator.naaccr.rule.AddressRule;

public class AddressCurrentRule extends AddressRule {

    // unique identifier for this rule
    public static final String ID = "address-current";

    private static final String _CRTIERIA = "Current Address is randomly generated based on SEER frequencies and the provided state";

    /**
     * Constructor.
     */
    public AddressCurrentRule() {
        super(ID,
                "Current address",
                "addressCurrentState",
                "addressCurrentCity",
                "addressCurrentPostalCode",
                "addressCurrentCountry",
                "addressCurrentStreetName",
                "addressCurrentSupplementl",
                "addressCurrentCounty");
    }

    @Override
    public String getDescription() {
        return _CRTIERIA;
    }
}
