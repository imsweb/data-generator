package com.imsweb.datagenerator.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * A distributed random value is based on frequencies (usually from a CSV file) that provide a percentage and one or several corresponding value.
 * <br/><br/>
 * For example, a distribution of 50.0 -> 1 (MALE) and 50.0 -> 2 (FEMALE) would provide 50% of value for MALE and 50% for female...
 * <br/><br/>
 * Note that the total of the distribution doesn't need to be 100%.
 */
public class DistributedRandomValueGenerator {

    // table of values and the frequency of each
    private Map<List<String>, Double> _distribution;

    // sum of all frequencies in the table
    private double _distSum;

    /**
     * Constructor - creates empty table
     */
    public DistributedRandomValueGenerator() {
        _distribution = new LinkedHashMap<>();
        _distSum = 0.0;
    }

    /**
     * Constructor - builds the frequency distribution table from comma separated data.
     * Frequencies should be in the second column of the comma separated data ('value,frequency')
     * @param resourceLocation location of frequency data
     */
    public DistributedRandomValueGenerator(String resourceLocation) {
        this();

        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceLocation);
        if (url == null)
            throw new IllegalStateException("Unable to find internal resource '" + resourceLocation + "'");

        try {
            InputStream is = url.openStream();
            if (resourceLocation.toLowerCase().endsWith(".gz"))
                is = new GZIPInputStream(is);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] splitLine = line.split(",");
                    double frequency = Double.parseDouble(splitLine[0]);
                    List<String> values = new ArrayList<>();
                    values.addAll(Arrays.asList(splitLine).subList(1, splitLine.length));
                    add(values, frequency);
                }
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to open internal resource '" + resourceLocation + "'", e);
        }
    }

    /**
     * Adds a list of new values to the frequency distribution table.
     * @param values list of values to add to generator
     * @param frequency frequency of values
     */
    public void add(List<String> values, double frequency) {
        _distribution.put(values, frequency);
        _distSum += frequency;
    }

    /**
     * Adds a single new value to the frequency distribution table
     * @param value value to add to generator
     * @param frequency frequency of value
     */
    public void add(String value, double frequency) {
        add(Collections.singletonList(value), frequency);
    }

    /**
     * Returns a random value from the frequency distribution table
     * @return random value
     */
    public String getRandomValue() {
        return getRandomValueList().get(0);
    }

    /**
     * Returns a list of associated random values from the frequency distribution table. Should be used
     * if multiple values for a single frequency are expected
     * @return list of random values
     */
    public List<String> getRandomValueList() {
        if (_distribution.isEmpty())
            throw new IllegalStateException("Random value generator contains no values");

        double rand = RandomUtils.nextDouble();
        double ratio = 1.0f / _distSum;
        double tempDist = 0;

        for (Entry<List<String>, Double> entry : _distribution.entrySet()) {
            tempDist += entry.getValue();
            if (rand / ratio <= tempDist)
                return entry.getKey();
        }
        // if nothing found, return the first value in the map
        return _distribution.entrySet().iterator().next().getKey();
    }
}
