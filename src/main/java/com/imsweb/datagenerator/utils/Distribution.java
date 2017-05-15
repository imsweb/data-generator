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

public class Distribution<T> {

    public static <D> Distribution<D> of(Map<D, Double> values) {
        return new Distribution<>(values.entrySet().stream().map(e -> new DistributionElementDto<>(e.getValue(), e.getKey())).collect(Collectors.toList()));
    }

    public static <D> Distribution<D> of(List<D> values) {
        return new Distribution<>(values.stream().map(e -> new DistributionElementDto<>(1D, e)).collect(Collectors.toList()));
    }

    public static Distribution<String> of(URL url) {
        return of(url, String.class, null);
    }

    @SuppressWarnings("unchecked")
    public static <D> Distribution<D> of(URL url, Class<D> valueType, Map<Integer, String> columnsMapping) {
        try {
            InputStream is = url.openStream();
            if (url.getPath().toLowerCase().endsWith(".gz"))
                is = new GZIPInputStream(is);

            List<DistributionElementDto<D>> elements = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                while (line != null) {
                    DistributionElementDto<D> element = new DistributionElementDto<>();
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
                        Object obj = valueType.newInstance();
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

    private List<DistributionElementDto<T>> _frequencies;

    private double _frequenciesTotal;

    public Distribution(List<DistributionElementDto<T>> frequencies) {
        _frequencies = frequencies;
        _frequencies.sort(Comparator.comparing(DistributionElementDto::getFrequency));
        _frequenciesTotal = _frequencies.stream().mapToDouble(DistributionElementDto::getFrequency).sum();
    }

    public List<DistributionElementDto<T>> getFrequencies() {
        return _frequencies;
    }

    public void setFrequencies(List<DistributionElementDto<T>> frequencies) {
        _frequencies = frequencies;
    }

    public T getValue() {
        double rand = RandomUtils.nextDouble();
        double ratio = 1.0f / _frequenciesTotal;
        double tempDist = 0;

        for (DistributionElementDto<T> element : _frequencies) {
            tempDist += element.getFrequency();
            if (rand / ratio <= tempDist)
                return element.getValue();
        }

        // if nothing found, return the first value (the most common one)
        return _frequencies.isEmpty() ? null : _frequencies.get(0).getValue();
    }
}
