/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * This class represents a distribution of frequencies and allows a random values to be requested based on those frequencies (so the most highest the frequency the
 * most likely that particular value will be returned).
 * @param <T> the type of the value
 */
public class Distribution<T> {

    /**
     * Creates a distribution from a given map of frequencies.
     * @param frequencies frequencies, required
     * @param <D> value type
     * @return a new distribution instance
     */
    public static <D> Distribution<D> of(Map<D, Double> frequencies) {
        return new Distribution<>(frequencies.entrySet().stream().map(e -> new DistributionElement<>(e.getValue(), e.getKey())).collect(Collectors.toList()));
    }

    /**
     * Creates a distribution from a given list of values (the same frequency will be assumed for all of the values).
     * @param values values, required
     * @param <D> value type
     * @return a new distribution instance
     */
    public static <D> Distribution<D> of(List<D> values) {
        return new Distribution<>(values.stream().map(e -> new DistributionElement<>(1D, e)).collect(Collectors.toList()));
    }

    /**
     * Creates a distribution from the given URl.
     * @param url target URL, required; must be a CSV file (possibly GZipped), each row can either have one element (the value) or two (the frequency and the value)
     * @return a new distribution instance
     */
    public static Distribution<String> of(URL url) {
        return of(url, String.class, null);
    }

    /**
     * Creates a distribution from the given URl using the given value type and columns mapping to create complex value objects.
     * @param url target URL, required, must be a CSV file (possibly GZipped)
     * @param valueType if rows have more than one values (in addition to the frequency), this will be used to instantiate complex value objects and map the values to them
     * @param columnsMapping if rows have more than one values (in addition to the frequency), this will be used to map the column index to fields in the complex objects
     * @param <D> value type
     * @return a new distribution instance
     */
    @SuppressWarnings("unchecked")
    public static <D> Distribution<D> of(URL url, Class<D> valueType, Map<Integer, String> columnsMapping) {
        try {
            InputStream is = url.openStream();
            if (url.getPath().toLowerCase().endsWith(".gz"))
                is = new GZIPInputStream(is);

            List<DistributionElement<D>> elements = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                while (line != null) {
                    DistributionElement<D> element = new DistributionElement<>();
                    String[] values = StringUtils.split(line, ',');
                    if (values.length == 1) {
                        element.setValue((D)values[0]);
                        element.setFrequency(1D);
                    }
                    else if (values.length == 2) {
                        element.setValue((D)values[1]);
                        element.setFrequency(Double.parseDouble(values[0]));
                    }
                    else {
                        Object obj = valueType.getDeclaredConstructor().newInstance();
                        for (Map.Entry<Integer, String> entry : columnsMapping.entrySet())
                            valueType.getMethod("set" + StringUtils.capitalize(entry.getValue()), String.class).invoke(obj, values[entry.getKey()]);
                        element.setValue((D)obj);
                        element.setFrequency(Double.parseDouble(values[0]));
                    }
                    elements.add(element);
                    line = reader.readLine();
                }
            }

            return new Distribution<>(elements);
        }
        catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Unable to process provided distribution URL", e);
        }
    }

    // frequencies
    private List<DistributionElement<T>> _frequencies;

    // frequencies total
    private double _frequenciesTotal;

    /**
     * Constructor.
     * @param frequencies frequencies, required
     */
    public Distribution(List<DistributionElement<T>> frequencies) {
        _frequencies = frequencies;
        _frequencies.sort(Comparator.comparing(DistributionElement::getFrequency));
        _frequenciesTotal = _frequencies.stream().mapToDouble(DistributionElement::getFrequency).sum();
    }

    public List<DistributionElement<T>> getFrequencies() {
        return _frequencies;
    }

    public void setFrequencies(List<DistributionElement<T>> frequencies) {
        _frequencies = frequencies;
    }

    /**
     * Returns a random value based on the current distribution of frequencies.
     * @return a random value, can be a simple type (like a string) or a complex object (like an address)
     */
    public T getValue() {
        double rand = RandomUtils.nextDouble();
        double ratio = 1.0f / _frequenciesTotal;
        double tempDist = 0;

        for (DistributionElement<T> element : _frequencies) {
            tempDist += element.getFrequency();
            if (rand / ratio <= tempDist)
                return element.getValue();
        }

        // if nothing found, return the first value (the most common one)
        return _frequencies.isEmpty() ? null : _frequencies.get(0).getValue();
    }
}
