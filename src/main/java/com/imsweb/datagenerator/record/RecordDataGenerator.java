package com.imsweb.datagenerator.record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.DataGenerator;
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
public class RecordDataGenerator implements DataGenerator {

    // the layout used for this generator (the layout defines the variables that can be used in the rules)
    private final RecordLayout _layout;

    // list of rules to be executed
    private final List<RecordDataGeneratorRule> _rules;

    /**
     * Constructor
     * @param layout record layout to use
     */
    public RecordDataGenerator(RecordLayout layout) {
        if (layout == null)
            throw new IllegalArgumentException("A layout is required for creating a record-based data generator!");
        _layout = layout;
        _rules = new ArrayList<>();
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    /**
     * Adds a new rule
     * @param rule rule to be added
     */
    public void addRule(RecordDataGeneratorRule rule) {
        _rules.add(rule);
    }

    /**
     * Returns all the rules from this generator.
     */
    public List<RecordDataGeneratorRule> getRules() {
        return _rules;
    }

    /**
     * Generates a single record.
     * @param options options that will be provided to every rules.
     * @return the created record
     */
    public Map<String, String> generateRecord(Map<String, Object> options) {
        Map<String, String> rec = new HashMap<>();

        for (RecordDataGeneratorRule rule : _rules)
            rule.execute(rec, options);

        return rec;
    }

    /**
     * Generates a requested number of records and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numRecords number of records to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numRecords, Map<String, Object> options) throws IOException {
        if (numRecords < 1)
            throw new IllegalArgumentException("Number of records must be greater than 0.");

        // handle a compress file
        boolean isGZip = file.getName().toLowerCase().endsWith(".gz");
        try (
                OutputStream os = Files.newOutputStream(file.toPath());
                Writer writer = new BufferedWriter(new OutputStreamWriter(isGZip ? new GZIPOutputStream(os) : os, StandardCharsets.UTF_8))
        ) {
            for (int i = 0; i < numRecords; i++)
                _layout.writeRecord(writer, generateRecord(options));
        }
    }
}
