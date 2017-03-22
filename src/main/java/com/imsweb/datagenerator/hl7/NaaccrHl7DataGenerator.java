package com.imsweb.datagenerator.hl7;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.hl7.rule.ControlSegmentRule;
import com.imsweb.datagenerator.hl7.rule.PatientIdentifierSegmentRule;
import com.imsweb.layout.hl7.NaaccrHl7Layout;
import com.imsweb.layout.hl7.entity.Hl7Message;

/**
 * A data generator can be used to create fake NAACCR HL7 data files.
 */
public class NaaccrHl7DataGenerator implements DataGenerator {

    // the layout used for this generator (the layout defines the variables that can be used in the rules)
    private NaaccrHl7Layout _layout;

    // list of rules to be executed
    private List<NaaccrHl7DataGeneratorRule> _rules;

    /**
     * Constructor
     * @param layout record layout to use
     */
    public NaaccrHl7DataGenerator(NaaccrHl7Layout layout) {
        if (layout == null)
            throw new RuntimeException("A layout is required for creating a NAACCR HL7 data generator!");
        _layout = layout;
        _rules = new ArrayList<>();

        _rules.add(new ControlSegmentRule());
        _rules.add(new PatientIdentifierSegmentRule());
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    /**
     * Generates a single message.
     * @param options options that will be provided to every rules.
     * @return the created message
     */
    public Hl7Message generateMessage(NaaccrHl7DataGeneratorOptions options) {
        Hl7Message message = new Hl7Message();

        for (NaaccrHl7DataGeneratorRule rule : _rules)
            rule.execute(message, options);

        return message;
    }

    /**
     * Generates a requested number of messages and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numMessages number of messages to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numMessages, NaaccrHl7DataGeneratorOptions options) throws IOException {
        if (numMessages < 1)
            throw new RuntimeException("Number of messages must be greater than 0.");

        // handle a compress file
        OutputStream os = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            os = new GZIPOutputStream(os);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            for (int i = 0; i < numMessages; i++) {
                _layout.writeMessage(writer, generateMessage(options));
                writer.newLine();
            }
        }
    }
}
