package com.imsweb.datagenerator.naaccr.rule.patient;

import com.imsweb.datagenerator.naaccr.rule.AddressRule;

public class AddressCurrentRule extends AddressRule {

    // unique identifier for this rule
    public static final String ID = "address-current";

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
}
