package com.imsweb.datagenerator.hl7;

import java.util.Map;

import com.imsweb.datagenerator.DataGeneratorRule;
import com.imsweb.layout.hl7.entity.Hl7Message;

public abstract class NaaccrHl7DataGeneratorRule implements DataGeneratorRule {

    // the unique ID of this rule
    private final String _id;

    // the name of this rule
    private final String _name;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    protected NaaccrHl7DataGeneratorRule(String id, String name) {
        _id = id;
        _name = name;
    }

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    /**
     * Execute the logic of the rule on the provided record.
     */
    public abstract void execute(Hl7Message message, NaaccrHl7DataGeneratorOptions options, Map<String, Object> context);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NaaccrHl7DataGeneratorRule)) return false;

        NaaccrHl7DataGeneratorRule that = (NaaccrHl7DataGeneratorRule)o;

        return _id.equals(that._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
