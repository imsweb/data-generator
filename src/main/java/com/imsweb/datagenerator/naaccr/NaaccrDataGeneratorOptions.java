/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.util.Map;

public class NaaccrDataGeneratorOptions {

    // if provided, each patient will have this number of tumors; otherwise a random distribution will be used.
    protected Integer _numTumorsPerPatient;

    // the state abbreviation (upper-cased) to use to generate the address information (no address info will be generated if left null)
    protected String _state;

    // properties to set to a specific value before running any rules (those would be overridden by the value set by a rule; they can be used as default values)
    protected Map<String, String> _constantValuesPreProcessing;

    // properties to set to a specific value after running any rules (those would override the value set by a rule)
    protected Map<String, String> _constantValuesPostProcessing;

    // maximum year of diagnosis (inclusive), current year if not provided
    protected Integer _maxDxYear;

    // minimum year of diagnosis (inclusive), max year - 10 if not provided
    protected Integer _minDxYear;

    // the value to use for DEAD vital status (SEER uses 4 and COC uses 0); defaults to 4
    protected String _vitalStatusDeadValue;

    public Integer getNumTumorsPerPatient() {
        return _numTumorsPerPatient;
    }

    public void setNumTumorsPerPatient(Integer numTumorsPerPatient) {
        _numTumorsPerPatient = numTumorsPerPatient;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    public Map<String, String> getConstantValuesPreProcessing() {
        return _constantValuesPreProcessing;
    }

    public void setConstantValuesPreProcessing(Map<String, String> constantValuesPreProcessing) {
        _constantValuesPreProcessing = constantValuesPreProcessing;
    }

    public Map<String, String> getConstantValuesPostProcessing() {
        return _constantValuesPostProcessing;
    }

    public void setConstantValuesPostProcessing(Map<String, String> constantValuesPostProcessing) {
        _constantValuesPostProcessing = constantValuesPostProcessing;
    }

    public Integer getMaxDxYear() {
        return _maxDxYear;
    }

    public void setMaxDxYear(Integer maxDxYear) {
        _maxDxYear = maxDxYear;
    }

    public Integer getMinDxYear() {
        return _minDxYear;
    }

    public void setMinDxYear(Integer minDxYear) {
        _minDxYear = minDxYear;
    }

    public String getVitalStatusDeadValue() {
        return _vitalStatusDeadValue;
    }

    public void setVitalStatusDeadValue(String vitalStatusDeadValue) {
        _vitalStatusDeadValue = vitalStatusDeadValue;
    }
}
