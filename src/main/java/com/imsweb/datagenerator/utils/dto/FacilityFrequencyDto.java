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
        switch (index) {
            case 0:
                return _specialty01;
            case 1:
                return _specialty02;
            case 2:
                return _specialty03;
            case 3:
                return _specialty04;
            case 4:
                return _specialty05;
            case 5:
                return _specialty06;
            case 6:
                return _specialty07;
            case 7:
                return _specialty08;
            case 8:
                return _specialty09;
            case 9:
                return _specialty10;
            case 10:
                return _specialty11;
            case 11:
                return _specialty12;
            case 12:
                return _specialty13;
            case 13:
                return _specialty14;
            case 14:
                return _specialty15;
            default:
                return "";
        }
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

    public void setSpecialty04(String specialty) {
        _specialty04 = specialty;
    }

    public void setSpecialty05(String specialty) {
        _specialty05 = specialty;
    }

    public void setSpecialty06(String specialty) {
        _specialty06 = specialty;
    }

    public void setSpecialty07(String specialty) {
        _specialty07 = specialty;
    }

    public void setSpecialty08(String specialty) {
        _specialty08 = specialty;
    }

    public void setSpecialty09(String specialty) {
        _specialty09 = specialty;
    }

    public void setSpecialty10(String specialty) {
        _specialty10 = specialty;
    }

    public void setSpecialty11(String specialty) {
        _specialty11 = specialty;
    }

    public void setSpecialty12(String specialty) {
        _specialty12 = specialty;
    }

    public void setSpecialty13(String specialty) {
        _specialty13 = specialty;
    }

    public void setSpecialty14(String specialty) {
        _specialty14 = specialty;
    }

    public void setSpecialty15(String specialty) {
        _specialty15 = specialty;
    }
}
