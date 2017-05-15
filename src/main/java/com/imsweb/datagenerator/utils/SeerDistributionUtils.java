/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.utils.dto.CityFrequencyDto;
import com.imsweb.datagenerator.utils.dto.SiteFrequencyDto;

/**
 * This class provides distributions based on frequencies taken from the SEER data.
 * <br/><br/>
 * The main purpose of this class is to provide caching for those distributions so the data is not loaded several times by different rules...
 */
public class SeerDistributionUtils {

    private static Distribution<String> _DIST_NAME_LAST_WHITE, _DIST_NAME_LAST_BLACK, _DIST_NAME_LAST_API, _DIST_NAME_LAST_HISP;
    private static Distribution<String> _DIST_RACE;
    private static Distribution<String> _DIST_HISPANIC_ORIGIN;
    private static Distribution<String> _DIST_SEX;
    private static Distribution<String> _DIST_VITAL_STATUS;
    private static Distribution<SiteFrequencyDto> _DIST_SITE_MALE, _DIST_SITE_FEMALE;
    private static Map<String, Distribution<CityFrequencyDto>> _DIST_CITIES = new HashMap<>();

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
        _DIST_RACE = null;
        _DIST_HISPANIC_ORIGIN = null;
        _DIST_SEX = null;
        _DIST_VITAL_STATUS = null;
        _DIST_SITE_MALE = null;
        _DIST_SITE_FEMALE = null;
        _DIST_CITIES.clear();
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
}
