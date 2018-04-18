/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.naaccr;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.utils.DistributionUtils;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;
import com.imsweb.datagenerator.utils.dto.PhysicianFrequencyDto;

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

    // Registry ID (NAACCR Item #40)
    protected String _registryId;

    // List of pre-created Facilities.
    protected List<FacilityFrequencyDto> _facilityList;

    // List of pre-created Physicians.
    protected List<PhysicianFrequencyDto> _physicianList;

    public Integer getNumTumorsPerPatient() {
        return _numTumorsPerPatient;
    }

    /**
     * @param numTumorsPerPatient must be between 1 and 10, inclusive
     */
    public void setNumTumorsPerPatient(Integer numTumorsPerPatient) {
        if (numTumorsPerPatient < 1 || numTumorsPerPatient > 10)
            throw new IllegalArgumentException("Number of tumors per patient must be between 1 and 10");
        _numTumorsPerPatient = numTumorsPerPatient;
    }

    public String getState() {
        return _state;
    }

    /**
     * @param state must be null, blank, or a valid two-letter abbreviation of a US state - null or blank state will prevent address-based rules from running
     */
    public void setState(String state) {
        if (state != null && !DistributionUtils.getAllStates().contains(state.toUpperCase()))
            throw new IllegalArgumentException("State '" + state + "' is not a valid US state");
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

    /**
     * @param maxDxYear maximum year of diagnosis (must be greater than or equal to min year of diagnosis and between 1900 and 2100, inclusive
     */
    public void setMaxDxYear(Integer maxDxYear) {
        if (maxDxYear < 1900 || maxDxYear > 2100)
            throw new IllegalArgumentException("Max DX Year must be between 1900 and 2100");
        if (_minDxYear != null && _minDxYear > maxDxYear)
            throw new IllegalArgumentException("Max DX Year must be greater than or equal to Min DX Year (" + _minDxYear + ")");
        _maxDxYear = maxDxYear;
    }

    public Integer getMinDxYear() {
        return _minDxYear;
    }

    /**
     * @param minDxYear minimum year of diagnosis (must be less than or equal to max year of diagnosis and between 1900 and 2100, inclusive
     */
    public void setMinDxYear(Integer minDxYear) {
        if (minDxYear < 1900 || minDxYear > 2100)
            throw new IllegalArgumentException("Min DX Year must be between 1900 and 2100");
        if (_maxDxYear != null && _maxDxYear < minDxYear)
            throw new IllegalArgumentException("Min DX Year must be less than or equal to Max DX Year (" + _maxDxYear + ")");
        _minDxYear = minDxYear;
    }

    public String getVitalStatusDeadValue() {
        return _vitalStatusDeadValue;
    }

    public void setVitalStatusDeadValue(String vitalStatusDeadValue) {
        _vitalStatusDeadValue = vitalStatusDeadValue;
    }

    /**
     * Returns min DX date.
     * If min DX year was not defined, this will return the current year minus ten years.
     * If min DX year was defined, this will return the first day of that year
     */
    public LocalDate getMinDxDate() {
        return _minDxYear == null ? LocalDate.now().minusYears(10) : LocalDate.of(_minDxYear, 1, 1);
    }

    /**
     * Returns max DX date.
     * If max DX year was not defined this will return the current date.
     * If max DX year was defined, this will return the last day of that year (if max DX year is current year, this could return a future date)
     */
    public LocalDate getMaxDxDate() {
        if (_maxDxYear == null || _maxDxYear == LocalDate.now().getYear())
            return LocalDate.now();
        return LocalDate.of(_maxDxYear, 12, 31);
    }

    public void setRegistryId(String registryId) {
        _registryId = registryId;
    }

    public String getRegistryId() {
        return _registryId;
    }

    public void setFacilities(List<FacilityFrequencyDto> facilityList) {
        _facilityList = facilityList;
    }

    public void setPhysicians(List<PhysicianFrequencyDto> physicianList) {
        _physicianList = physicianList;
    }

    /**
     * Returns a list of pre-created Facilities for this state.
     */
    public List<FacilityFrequencyDto> getFacilities() {
        return _facilityList;
    }

    /**
     * Returns a list of pre-created Physicians for this state.
     */
    public List<PhysicianFrequencyDto> getPhysicians() {
        return _physicianList;
    }

}
