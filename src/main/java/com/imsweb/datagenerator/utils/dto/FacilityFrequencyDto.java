/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

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
    private String _specialty04;
    private String _specialty05;
    private String _specialty06;
    private String _specialty07;
    private String _specialty08;
    private String _specialty09;
    private String _specialty10;
    private String _specialty11;
    private String _specialty12;
    private String _specialty13;
    private String _specialty14;
    private String _specialty15;

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
        String retval = "";
        switch (index) {
            case 0:
                retval = _specialty01;
                break;
            case 1:
                retval = _specialty02;
                break;
            case 2:
                retval = _specialty03;
                break;
            case 3:
                retval = _specialty04;
                break;
            case 4:
                retval = _specialty05;
                break;
            case 5:
                retval = _specialty06;
                break;
            case 6:
                retval = _specialty07;
                break;
            case 7:
                retval = _specialty08;
                break;
            case 8:
                retval = _specialty09;
                break;
            case 9:
                retval = _specialty10;
                break;
            case 10:
                retval = _specialty11;
                break;
            case 11:
                retval = _specialty12;
                break;
            case 12:
                retval = _specialty13;
                break;
            case 13:
                retval = _specialty14;
                break;
            case 14:
                retval = _specialty15;
                break;
        }
        return retval;
    }

    public void setSpecialty01(String specialty) { _specialty01 = specialty; }

    public void setSpecialty02(String specialty) { _specialty02 = specialty; }

    public void setSpecialty03(String specialty) { _specialty03 = specialty; }

    public void setSpecialty04(String specialty) { _specialty04 = specialty; }

    public void setSpecialty05(String specialty) { _specialty05 = specialty; }

    public void setSpecialty06(String specialty) { _specialty06 = specialty; }

    public void setSpecialty07(String specialty) { _specialty07 = specialty; }

    public void setSpecialty08(String specialty) { _specialty08 = specialty; }

    public void setSpecialty09(String specialty) { _specialty09 = specialty; }

    public void setSpecialty10(String specialty) { _specialty10 = specialty; }

    public void setSpecialty11(String specialty) { _specialty11 = specialty; }

    public void setSpecialty12(String specialty) { _specialty12 = specialty; }

    public void setSpecialty13(String specialty) { _specialty13 = specialty; }

    public void setSpecialty14(String specialty) { _specialty14 = specialty; }

    public void setSpecialty15(String specialty) { _specialty15 = specialty; }
}
