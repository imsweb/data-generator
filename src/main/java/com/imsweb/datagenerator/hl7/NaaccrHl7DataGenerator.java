package com.imsweb.datagenerator.hl7;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.DataGenerator;
import com.imsweb.datagenerator.hl7.rule.CommonOrderSegmentRule;
import com.imsweb.datagenerator.hl7.rule.ControlSegmentRule;
import com.imsweb.datagenerator.hl7.rule.NextOfKinSegmentRule;
import com.imsweb.datagenerator.hl7.rule.ObservationRequestSegmentRule;
import com.imsweb.datagenerator.hl7.rule.ObservationSegmentRule;
import com.imsweb.datagenerator.hl7.rule.PatientIdentifierSegmentRule;
import com.imsweb.datagenerator.hl7.rule.PatientVisitSegmentRule;
import com.imsweb.datagenerator.hl7.rule.SpecimenSegmentRule;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.hl7.NaaccrHl7Layout;
import com.imsweb.layout.hl7.entity.Hl7Message;
import com.imsweb.seerutils.SeerUtils;

/**
 * A data generator can be used to create fake NAACCR HL7 data files.
 */
@SuppressWarnings("unused")
public class NaaccrHl7DataGenerator implements DataGenerator {

    // the layout used for this generator (the layout defines the variables that can be used in the rules)
    protected NaaccrHl7Layout _layout;

    // list of rules to be executed
    protected List<NaaccrHl7DataGeneratorRule> _rules;

    /**
     * Constructor
     * @param layoutId NAACCR HL7 layout ID to use
     */
    public NaaccrHl7DataGenerator(String layoutId) {
        this(LayoutFactory.getNaaccrHl7Layout(layoutId));
    }

    /**
     * Constructor
     * @param layout Naaccr HL7 layout to use
     */
    public NaaccrHl7DataGenerator(NaaccrHl7Layout layout) {
        if (layout == null)
            throw new IllegalStateException("A layout is required for creating a NAACCR HL7 data generator!");
        _layout = layout;

        _rules = new ArrayList<>();
        _rules.add(new ControlSegmentRule());
        _rules.add(new PatientIdentifierSegmentRule());
        _rules.add(new NextOfKinSegmentRule());
        _rules.add(new PatientVisitSegmentRule());
        _rules.add(new CommonOrderSegmentRule());
        _rules.add(new ObservationRequestSegmentRule());
        _rules.add(new SpecimenSegmentRule());
        _rules.add(new ObservationSegmentRule());
    }

    @Override
    public String getId() {
        return _layout.getLayoutId();
    }

    /**
     * Adds a new rule
     * @param rule rule to be added
     */
    public void addRule(NaaccrHl7DataGeneratorRule rule) {
        _rules.add(rule);
    }

    /**
     * Removes the requested rule.
     * @param ruleId rule ID to remove
     * @return true if the rule was actually removed
     */
    public boolean removeRule(String ruleId) {
        return _rules.remove(getRule(ruleId));
    }

    /**
     * Replaced an existing rule by the provided rule, both rules must have the same ID.
     * @param newRule new rule to replace with
     * @return true if the rule was actually replaced
     */
    public boolean replaceRule(NaaccrHl7DataGeneratorRule newRule) {
        int idx = _rules.indexOf(newRule);
        if (idx != -1) {
            _rules.set(idx, newRule);
            return true;
        }
        return false;
    }

    /**
     * Returns the requested rule from this generator.
     */
    public NaaccrHl7DataGeneratorRule getRule(String ruleId) {
        for (NaaccrHl7DataGeneratorRule rule : _rules)
            if (ruleId.equals(rule.getId()))
                return rule;
        return null;
    }

    /**
     * Returns all the rules from this generator.
     */
    public List<NaaccrHl7DataGeneratorRule> getRules() {
        return _rules;
    }

    /**
     * Generates a single message.
     * @return the created message
     */
    public Hl7Message generateMessage() {
        return generateMessage(null);
    }

    /**
     * Generates a single message.
     * @param options options that will be provided to every rules.
     * @return the created message
     */
    public Hl7Message generateMessage(NaaccrHl7DataGeneratorOptions options) {
        Hl7Message message = new Hl7Message();

        Map<String, Object> context = new HashMap<>();
        for (NaaccrHl7DataGeneratorRule rule : _rules)
            rule.execute(message, options, context);

        return message;
    }

    /**
     * Generates a requested number of messages and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numMessages number of messages to generate, must be greater than 0
     */
    public void generateFile(File file, int numMessages) throws IOException {
        generateFile(file, numMessages, null);
    }

    /**
     * Generates a requested number of messages and saves them in the specified file.
     * @param file file to create; if the name ends with ".gz", it will be compressed
     * @param numMessages number of messages to generate, must be greater than 0
     * @param options options that will be provided to every rules.
     */
    public void generateFile(File file, int numMessages, NaaccrHl7DataGeneratorOptions options) throws IOException {
        if (numMessages < 1)
            throw new IllegalStateException("Number of messages must be greater than 0.");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(SeerUtils.createOutputStream(file), StandardCharsets.UTF_8))) {
            for (int i = 0; i < numMessages; i++) {
                _layout.writeMessage(writer, generateMessage(options));
                writer.newLine();
            }
        }
    }
}
