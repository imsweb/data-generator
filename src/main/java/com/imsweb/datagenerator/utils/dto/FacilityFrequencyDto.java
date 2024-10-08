/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public class FacilityFrequencyDto {

    private String _npi;
    private String _name;
    private String _addressFirstLine;
    private String _addressSecondLine;
    private String _addressCity;
    private String _addressState;
    private String _addressPostalCode;
    private String _addressTelephone;
    private String _specialty01;
    private String _specialty02;
    private String _specialty03;

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

    public String getAddressFirstLine() {
        return _addressFirstLine;
    }

    public void setAddressFirstLine(String addressFirstLine) {
        _addressFirstLine = addressFirstLine;
    }

    public String getAddressSecondLine() {
        return _addressSecondLine;
    }

    public void setAddressSecondLine(String addressSecondLine) {
        _addressSecondLine = addressSecondLine;
    }

    public String getAddressCity() {
        return _addressCity;
    }

    public void setAddressCity(String addressCity) {
        _addressCity = addressCity;
    }

    public String getAddressState() {
        return _addressState;
    }

    public void setAddressState(String addressState) {
        _addressState = addressState;
    }

    public String getAddressPostalCode() {
        return _addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        _addressPostalCode = addressPostalCode;
    }

    public String getAddressTelephone() {
        return _addressTelephone;
    }

    public void setAddressTelephone(String addressTelephone) {
        _addressTelephone = addressTelephone;
    }

    public String getSpecialty(int index) {
        if (index == 0)
            return _specialty01;
        else if (index == 1)
            return _specialty02;
        else if (index == 2)
            return _specialty03;
        else return "";
    }

    public void setSpecialty01(String specialty) {
        _specialty01 = specialty;
    }

    public void setSpecialty02(String specialty) {
        _specialty02 = specialty;
    }

    public void setSpecialty03(String specialty) {
        _specialty03 = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FacilityFrequencyDto))
            return false;
        FacilityFrequencyDto that = (FacilityFrequencyDto)o;
        return Objects.equals(_npi, that._npi)
               && Objects.equals(_name, that._name)
               && Objects.equals(_addressFirstLine, that._addressFirstLine)
               && Objects.equals(_addressSecondLine, that._addressSecondLine)
               && Objects.equals(_addressCity, that._addressCity)
               && Objects.equals(_addressState, that._addressState)
               && Objects.equals(_addressPostalCode, that._addressPostalCode)
               && Objects.equals(_addressTelephone, that._addressTelephone)
               && Objects.equals(_specialty01, that._specialty01)
               && Objects.equals(_specialty02, that._specialty02)
               && Objects.equals(_specialty03, that._specialty03);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_npi,
                _name,
                _addressFirstLine,
                _addressSecondLine,
                _addressCity,
                _addressState,
                _addressPostalCode,
                _addressTelephone,
                _specialty01,
                _specialty02,
                _specialty03);
    }
}
