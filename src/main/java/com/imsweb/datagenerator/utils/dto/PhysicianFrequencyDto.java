/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

public class PhysicianFrequencyDto {

    private String _npi;
    private String _lastName;
    private String _firstName;
    private String _middleName;
    private String _namePrefix;
    private String _nameSuffix;
    private String _credentials;
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

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getMiddleName() {
        return _middleName;
    }

    public void setMiddleName(String middleName) {
        _middleName = middleName;
    }

    public String getNamePrefix() {
        return _namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        _namePrefix = namePrefix;
    }

    public String getNameSuffix() {
        return _nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        _nameSuffix = nameSuffix;
    }

    public String getCredentials() {
        return _credentials;
    }

    public void setCredentials(String credentials) {
        _credentials = credentials;
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
}

