package com.imsweb.datagenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.imsweb.layout.record.RecordLayout;

/**
 * A data generator can be used to create fake data files for any fixed-columns or comma-separated formats.
 * <br/><br/>
 * It can also be used to create in-memory records represented by maps of properties.
 * <br/><br/>
 * The file format is defined by the layout provided to the constructor; that layout also defines the variables that can be populated by the rules.
 * <br/><br/>
 * By default, the data generator contains no rules. They have to be added before any record can be created.
 */
public class DataGenerator {

    // the layout used for this generator (the layout defines the variables that can be used in the rules)
    private RecordLayout _layout;

    // list of rules to be executed
    private List<DataGeneratorRule> _rules;

    /**
     * Constructor
     * @param layout record layout to use
     */
    public DataGenerator(RecordLayout layout) {
        if (layout == null)
            throw new RuntimeException("A layout is required for creating a data generator!");
        _layout = layout;
        _rules = new ArrayList<>();
    }

    /**
     * Adds a new rule
     * @param rule rule to be added
     */
    public void addRule(DataGeneratorRule rule) {
        _rules.add(rule);
    }

    /**
     * Returns all the rules from this generator.
     */
    public List<DataGeneratorRule> getRules() {
        return _rules;
    }

    /**
     * Generates a single record.
     * @param options options that will be provided to every rules.
     * @return the created record
     */
    public Map<String, String> generateRecord(Map<String, Object> options) {
        Map<String, String> record = new HashMap<>();

        for (DataGeneratorRule rule : _rules)
            rule.execute(record, options);

        return record;
    }

    /**
     * Generates a requested number of records and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numRecords number of records to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numRecords, Map<String, Object> options) throws IOException {
        if (numRecords < 1)
            throw new RuntimeException("Number of records must be greater than 0.");

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            for (int i = 0; i < numRecords; i++)
                _layout.writeRecord(writer, generateRecord(options));
        }
    }
}
