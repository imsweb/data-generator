/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility;

public class FacilityDto {

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
        if (this == o) return true;
        if (!(o instanceof FacilityDto)) return false;

        FacilityDto that = (FacilityDto)o;

        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return (_npi + _name).hashCode();
    }
}
