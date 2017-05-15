/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils.dto;

public class CityFrequencyDto {

    private String _zip;

    private String _city;

    private String _state;

    private String _longitude;

    private String _latitude;

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getLongitude() {
        return _longitude;
    }

    public void setLongitude(String longitude) {
        _longitude = longitude;
    }

    public String getLatitude() {
        return _latitude;
    }

    public void setLatitude(String latitude) {
        _latitude = latitude;
    }
}
