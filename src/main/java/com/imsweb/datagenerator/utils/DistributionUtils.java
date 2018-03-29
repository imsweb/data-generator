/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;
import com.imsweb.datagenerator.utils.dto.FacilityFrequencyDto;
import com.imsweb.datagenerator.utils.dto.PhysicianFrequencyDto;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

/**
 * This class provides distributions based on frequencies taken from the SEER data.
 * <br/><br/>
 * The main purpose of this class is to provide caching for those distributions so the data is not loaded several times by different rules...
 */
public class DistributionUtils {

    private static Distribution<String> _DIST_NAME_LAST_WHITE, _DIST_NAME_LAST_BLACK, _DIST_NAME_LAST_API, _DIST_NAME_LAST_HISP;
    private static Distribution<String> _DIST_NAME_FIRST_MALE, _DIST_NAME_FIRST_FEMALE;
    private static Distribution<String> _DIST_RACE;
    private static Distribution<String> _DIST_HISPANIC_ORIGIN;
    private static Distribution<String> _DIST_SEX;
    private static Distribution<String> _DIST_VITAL_STATUS;
    private static Distribution<SiteFrequencyDto> _DIST_SITE_MALE, _DIST_SITE_FEMALE;
    private static Distribution<String> _DIST_STREET_NAME;
    private static Distribution<String> _DIST_STREET_SUFFIX;
    private static Map<String, Distribution<CityFrequencyDto>> _DIST_CITIES = new HashMap<>();
    private static Distribution<String> _DIST_STATE;
    private static Map<String, Distribution<FacilityFrequencyDto>> _DIST_FACILITIES = new HashMap<>();
    private static Map<String, Distribution<PhysicianFrequencyDto>> _DIST_PHYSICIANS = new HashMap<>();




    // API races
    private static List<String> _API_RACES = Arrays.asList("04", "05", "06", "07", "08", "10", "11", "12", "13", "14", "15", "16", "17", "20", "21", "22", "25", "26", "27", "28", "30", "31", "32",
            "96", "97");

    // states
    private static List<String> _STATES = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI",
            "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");

    /**
     * Clears all the cached distributions, forcing them to be (lazily) re-initialized if needed.
     */
    public static void clearCache() {
        _DIST_NAME_LAST_WHITE = null;
        _DIST_NAME_LAST_BLACK = null;
        _DIST_NAME_LAST_API = null;
        _DIST_NAME_LAST_HISP = null;
        _DIST_NAME_FIRST_MALE = null;
        _DIST_NAME_FIRST_FEMALE = null;
        _DIST_RACE = null;
        _DIST_HISPANIC_ORIGIN = null;
        _DIST_SEX = null;
        _DIST_VITAL_STATUS = null;
        _DIST_SITE_MALE = null;
        _DIST_SITE_FEMALE = null;
        _DIST_STREET_NAME = null;
        _DIST_STREET_SUFFIX = null;
        _DIST_CITIES.clear();
        _DIST_STATE = null;
        _DIST_FACILITIES.clear();
        _DIST_PHYSICIANS.clear();
    }

    public static List<String> getAllStates() {
        return _STATES;
    }

    public static String getNameLast() {
        return getNameLast(null, null);
    }

    public static String getNameLast(String hispanicOrigin, String race) {
        if (_DIST_NAME_LAST_WHITE == null) {
            _DIST_NAME_LAST_WHITE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/last_names_white.csv"));
            _DIST_NAME_LAST_BLACK = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/last_names_black.csv"));
            _DIST_NAME_LAST_API = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/last_names_api.csv"));
            _DIST_NAME_LAST_HISP = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/last_names_hispanic.csv"));
        }
        if (hispanicOrigin != null && !"0".equals(hispanicOrigin))
            return _DIST_NAME_LAST_HISP.getValue();
        if ("02".equals(race))
            return _DIST_NAME_LAST_BLACK.getValue();
        if (_API_RACES.contains(race))
            return _DIST_NAME_LAST_API.getValue();
        return _DIST_NAME_LAST_WHITE.getValue();
    }

    public static String getNameFirst() {
        return getNameFirst(null);
    }

    public static String getNameFirst(String sex) {
        if (_DIST_NAME_FIRST_MALE == null) {
            _DIST_NAME_FIRST_MALE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/first_names_male.csv"));
            _DIST_NAME_FIRST_FEMALE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/first_names_female.csv"));
        }
        if ("2".equals(sex))
            return _DIST_NAME_FIRST_FEMALE.getValue();
        return _DIST_NAME_FIRST_MALE.getValue();
    }

    public static String getRace() {
        if (_DIST_RACE == null)
            _DIST_RACE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/race.csv"));
        return _DIST_RACE.getValue();
    }

    public static String getHispanicOrigin() {
        if (_DIST_HISPANIC_ORIGIN == null)
            _DIST_HISPANIC_ORIGIN = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/hispanic_origin.csv"));
        return _DIST_HISPANIC_ORIGIN.getValue();
    }

    public static String getSex() {
        if (_DIST_SEX == null)
            _DIST_SEX = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/sex.csv"));
        return _DIST_SEX.getValue();
    }

    public static String getVitalStatus() {
        if (_DIST_VITAL_STATUS == null)
            _DIST_VITAL_STATUS = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/vital_status.csv"));
        return _DIST_VITAL_STATUS.getValue();
    }

    public static SiteFrequencyDto getSite() {
        return getSite(null);
    }

    public static SiteFrequencyDto getSite(String sex) {
        if (_DIST_SITE_MALE == null) {
            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(1, "site");
            mapping.put(2, "histology");
            mapping.put(3, "behavior");
            _DIST_SITE_MALE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/sites_sex_male.csv"), SiteFrequencyDto.class, mapping);
            _DIST_SITE_FEMALE = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/sites_sex_female.csv"), SiteFrequencyDto.class, mapping);
        }
        if ("2".equals(sex))
            return _DIST_SITE_FEMALE.getValue();
        return _DIST_SITE_MALE.getValue();
    }

    public static String getStreetName() {
        if (_DIST_STREET_NAME == null)
            _DIST_STREET_NAME = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/street_names.csv"));
        return _DIST_STREET_NAME.getValue();
    }

    public static String getStreetSuffix() {
        if (_DIST_STREET_SUFFIX == null)
            _DIST_STREET_SUFFIX = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/street_suffixes.csv"));
        return _DIST_STREET_SUFFIX.getValue();
    }

    public static CityFrequencyDto getCity() {
        return getCity(null);
    }

    public static CityFrequencyDto getCity(String state) {
        if (state == null || !_STATES.contains(state.toUpperCase()))
            state = "MD";

        Distribution<CityFrequencyDto> distribution = _DIST_CITIES.get(state.toLowerCase());
        if (distribution == null) {
            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(1, "zip");
            mapping.put(2, "city");
            mapping.put(3, "state");
            mapping.put(4, "longitude");
            mapping.put(5, "latitude");
            distribution = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/zip_codes/" + state.toLowerCase() + ".csv"), CityFrequencyDto.class, mapping);
            _DIST_CITIES.put(state.toLowerCase(), distribution);
        }
        return distribution.getValue();
    }

    public static String getState() {
        if (_DIST_STATE == null)
            _DIST_STATE = Distribution.of(_STATES);
        return _DIST_STATE.getValue();
    }

    public static FacilityFrequencyDto getFacility() {
        return getFacility(null);
    }

    public static FacilityFrequencyDto getFacility(String state) {
        if (state == null || !_STATES.contains(state.toUpperCase()))
            state = "MD";

        Distribution<FacilityFrequencyDto> distribution = _DIST_FACILITIES.get(state.toLowerCase());
        if (distribution == null) {
            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(1, "npi");
            mapping.put(2, "name");
            mapping.put(3, "addressFirstLine");
            mapping.put(4, "addressSecondLine");
            mapping.put(5, "addressCity");
            mapping.put(6, "addressState");
            mapping.put(7, "addressPostalCode");
            mapping.put(8, "addressTelephone");
            mapping.put(9, "specialty01");
            mapping.put(10, "specialty02");
            mapping.put(11, "specialty03");
            mapping.put(12, "specialty04");
            mapping.put(13, "specialty05");
            mapping.put(14, "specialty06");
            mapping.put(15, "specialty07");
            mapping.put(16, "specialty08");
            mapping.put(17, "specialty09");
            mapping.put(18, "specialty10");
            mapping.put(19, "specialty11");
            mapping.put(20, "specialty12");
            mapping.put(21, "specialty13");
            mapping.put(22, "specialty14");
            mapping.put(23, "specialty15");

            distribution = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/providers/Facility_" + state.toLowerCase() + ".csv"), FacilityFrequencyDto.class,
                    mapping);
            _DIST_FACILITIES.put(state.toLowerCase(), distribution);
        }
        return distribution.getValue();
    }

    public static PhysicianFrequencyDto getPhysician() {
        return getPhysician(null);
    }

    public static PhysicianFrequencyDto getPhysician(String state) {
        if (state == null || !_STATES.contains(state.toUpperCase()))
            state = "MD";

        Distribution<PhysicianFrequencyDto> distribution = _DIST_PHYSICIANS.get(state.toLowerCase());
        if (distribution == null) {
            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(1, "npi");
            mapping.put(2, "lastName");
            mapping.put(3, "firstName");
            mapping.put(4, "middleName");
            mapping.put(5, "namePrefix");
            mapping.put(6, "nameSuffix");
            mapping.put(7, "credentials");
            mapping.put(8, "addressFirstLine");
            mapping.put(9, "addressSecondLine");
            mapping.put(10, "addressCity");
            mapping.put(11, "addressState");
            mapping.put(12, "addressPostalCode");
            mapping.put(13, "addressTelephone");
            mapping.put(14, "specialty01");
            mapping.put(15, "specialty02");
            mapping.put(16, "specialty03");
            mapping.put(17, "specialty04");
            mapping.put(18, "specialty05");
            mapping.put(19, "specialty06");
            mapping.put(20, "specialty07");
            mapping.put(21, "specialty08");
            mapping.put(22, "specialty09");
            mapping.put(23, "specialty10");
            mapping.put(24, "specialty11");
            mapping.put(25, "specialty12");
            mapping.put(26, "specialty13");
            mapping.put(27, "specialty14");
            mapping.put(28, "specialty15");

            distribution = Distribution.of(Thread.currentThread().getContextClassLoader().getResource("frequencies/providers/Physician_" + state.toLowerCase() + ".csv"), PhysicianFrequencyDto.class,
                    mapping);
            _DIST_PHYSICIANS.put(state.toLowerCase(), distribution);
        }
        return distribution.getValue();
    }


}
