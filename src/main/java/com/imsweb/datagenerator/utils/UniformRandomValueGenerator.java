package com.imsweb.datagenerator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class UniformRandomValueGenerator {

    // list of all possible values
    private List<String> _values;

    /**
     * Constructor - creates empty list of possible values
     */
    public UniformRandomValueGenerator() {
        _values = new ArrayList<>();
    }

    /**
     * Constructor - Builds the list of possible values from a url
     * @param resourceLocation location of value list
     */
    public UniformRandomValueGenerator(String resourceLocation) {
        this();

        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceLocation);
        if (url == null)
            throw new IllegalStateException("Resource at '" + resourceLocation + "' could not be found");

        try {
            InputStream is = url.openStream();
            if (resourceLocation.toLowerCase().endsWith(".gz"))
                is = new GZIPInputStream(is);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null)
                    add(line);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Add a value to the pool of possible values. All values are added with equal probability.
     * @param value value to add
     */
    public void add(String value) {
        _values.add(value);
    }

    /**
     * Returns a random value from the pool of possible values
     * @return random value
     */
    public String getRandomValue() {
        if (_values.isEmpty())
            throw new IllegalStateException("Random value generator contains no values");

        return _values.get(RandomUtils.nextInt(_values.size()));
    }
}
