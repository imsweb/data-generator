/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

public class FacilityFrequencyDto {

    private String _npi;
    private String _name;
    private String _addressState;

    public String getNpi() {
        return _npi;
    }

    public void setNpi(String npi) {
        _npi = npi;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getAddressState() {
        return _addressState;
    }

    public void setAddressState(String addressState) {
        _addressState = addressState;
    }

}
