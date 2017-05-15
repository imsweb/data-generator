/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

public class DistributionElementDto<T> {

    private Double _frequency;

    private T _value;

    public DistributionElementDto() {
    }

    public DistributionElementDto(Double frequency, T value) {
        _frequency = frequency;
        _value = value;
    }

    public Double getFrequency() {
        return _frequency;
    }

    public void setFrequency(Double frequency) {
        _frequency = frequency;
    }

    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }
}
