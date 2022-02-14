/*
 * Copyright (C) 2022 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("ConstantConditions")
public class StagingUtils {

    private static Map<String, String> _KEYS;

    private static Map<String, Map<String, Distribution<String>>> _CS_DATA, _TNM_DATA, _EOD_DATA;

    private static final Pattern _STARTS_WITH_LETTER_PATTERN = Pattern.compile("^([A-Z]).+");

    /**
     * Clears all the cached data, forcing it to be (lazily) re-initialized if needed.
     */
    public static void clearCache() {
        _KEYS = null;
        _CS_DATA = null;
        _TNM_DATA = null;
        _EOD_DATA = null;
    }

    /**
     * Schema ID and field ID (NAACCR XML ID) are coded in the data (for optimization); this method can be used to "decode" a key.
     */
    public static String unformatKey(String key) {
        if (_KEYS == null) {
            _KEYS = new HashMap<>();
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("frequencies/staging_keys.csv");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line = reader.readLine();
                while (line != null) {
                    String[] parts = StringUtils.split(line, ',');
                    _KEYS.put(parts[1], parts[0]);
                    line = reader.readLine();
                }

            }
            catch (IOException e) {
                throw new RuntimeException("Unable to read staging keys", e);
            }
        }
        return _KEYS.get(key);
    }

    /**
     * Returns a random valid CS values for each relevant field (NAACCR XML ID) of the provided (coded) schema.
     */
    public static Map<String, String> getCsValues(String csSchemaKey) {
        if (StringUtils.isBlank(csSchemaKey))
            return new HashMap<>();

        if (_CS_DATA == null)
            _CS_DATA = loadFile("staging_cs.csv");

        Map<String, Distribution<String>> data = _CS_DATA.get(csSchemaKey);
        if (data == null)
            throw new RuntimeException("Unable to find map for CS staging key " + csSchemaKey);

        return getStagingValues(data);
    }

    /**
     * Returns a random valid TNM values for each relevant field (NAACCR XML ID) of the provided (coded) schema.
     */
    public static Map<String, String> getTnmValues(String tnmSchemaKey) {
        if (StringUtils.isBlank(tnmSchemaKey))
            return new HashMap<>();

        if (_TNM_DATA == null)
            _TNM_DATA = loadFile("staging_tnm.csv");

        Map<String, Distribution<String>> data = _TNM_DATA.get(tnmSchemaKey);
        if (data == null)
            throw new RuntimeException("Unable to find map for TNM staging key " + tnmSchemaKey);

        return getStagingValues(data);
    }

    /**
     * Returns a random valid EOD values for each relevant field (NAACCR XML ID) of the provided (coded) schema.
     */
    public static Map<String, String> getEodValues(String eodSchemaKey) {
        if (StringUtils.isBlank(eodSchemaKey))
            return new HashMap<>();

        if (_EOD_DATA == null)
            _EOD_DATA = loadFile("staging_eod.csv");

        Map<String, Distribution<String>> data = _EOD_DATA.get(eodSchemaKey);
        if (data == null)
            throw new RuntimeException("Unable to find map for EOD staging key " + eodSchemaKey);

        return getStagingValues(data);
    }

    private static Map<String, String> getStagingValues(Map<String, Distribution<String>> data) {
        Map<String, String> result = new HashMap<>();

        for (Entry<String, Distribution<String>> entry : data.entrySet()) {
            String val = entry.getValue().getValue();
            int idx = val.indexOf('-');
            if (idx != -1 && idx != val.length() - 1) { // deal with something like "p0I-"
                String lowVal = val.substring(0, idx);
                String highVal = val.substring(idx + 1);

                // deal with something like "A0.1-A9.9"
                String firstLetter = "";
                Matcher lowMather = _STARTS_WITH_LETTER_PATTERN.matcher(lowVal);
                if (lowMather.matches()) {
                    firstLetter = lowMather.group(1);
                    lowVal = lowVal.substring(1);
                }
                Matcher highMather = _STARTS_WITH_LETTER_PATTERN.matcher(highVal);
                if (highMather.matches())
                    highVal = highVal.substring(1);

                // deal with something like "0.1-999.9" (use 1-9999 for computing the value the re-apply the period)
                int idxLow = lowVal.indexOf('.');
                int idxHigh = highVal.indexOf('.');
                if (idxLow != -1 && idxHigh != -1) {
                    int low = Integer.parseInt(lowVal.replace(".", ""));
                    int high = Integer.parseInt(highVal.replace(".", ""));
                    result.put(unformatKey(entry.getKey()), firstLetter + reformatValueWithPeriod(StringUtils.leftPad(String.valueOf(RandomUtils.nextIntInRange(low, high)), 2, '0')));
                }
                else {
                    int low = Integer.parseInt(lowVal);
                    int high = Integer.parseInt(highVal);
                    result.put(unformatKey(entry.getKey()), firstLetter + StringUtils.leftPad(String.valueOf(RandomUtils.nextIntInRange(low, high)), (val.length() - 1) / 2, '0'));
                }
            }
            else
                result.put(unformatKey(entry.getKey()), val);
        }

        return result;
    }

    static String reformatValueWithPeriod(String value) {
        return value.substring(0, value.length() - 1) + "." + value.substring(value.length() - 1);
    }

    private static Map<String, Map<String, Distribution<String>>> loadFile(String filename) {
        Map<String, Map<String, Distribution<String>>> result = new HashMap<>();

        String line = null;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("frequencies/" + filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            line = reader.readLine();
            while (line != null) {
                String[] parts = StringUtils.split(line, ',');
                String schemaKey = parts[0];
                String fieldKey = parts[1];
                String[] values = StringUtils.split(parts[2], ';');

                result.computeIfAbsent(schemaKey, k -> new HashMap<>()).put(fieldKey, createDistribution(values));

                line = reader.readLine();
            }

        }
        catch (RuntimeException | IOException e) {
            throw new RuntimeException("Unable to read staging data: " + line, e);
        }

        return result;
    }

    static Distribution<String> createDistribution(String[] values) {
        Map<String, Double> frequencies = new HashMap<>(values.length);

        for (String val : values) {
            int idx = val.indexOf('-');
            if (idx != -1 && idx != val.length() - 1) { // deal with something like "p0I-"
                String lowVal = val.substring(0, idx);
                if (_STARTS_WITH_LETTER_PATTERN.matcher(lowVal).matches())
                    lowVal = lowVal.substring(1);
                String highVal = val.substring(idx + 1);
                if (_STARTS_WITH_LETTER_PATTERN.matcher(highVal).matches())
                    highVal = highVal.substring(1);

                // deal with something like "0.1-999.9" (use 1-9999 for computing the frequencies)
                int idxLow = lowVal.indexOf('.');
                int idxHigh = highVal.indexOf('.');
                if (idxLow != -1 && idxHigh != -1)
                    frequencies.put(val, (double)Integer.parseInt(highVal.replace(".", "")) - Integer.parseInt(lowVal.replace(".", "")) + 1);
                else
                    frequencies.put(val, (double)Integer.parseInt(highVal) - Integer.parseInt(lowVal) + 1);
            }
            else
                frequencies.put(val, 1D);
        }

        return Distribution.of(frequencies);
    }
}
