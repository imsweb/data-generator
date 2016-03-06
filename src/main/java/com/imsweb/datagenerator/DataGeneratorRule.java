package com.imsweb.datagenerator;

import java.util.Map;

public abstract class DataGeneratorRule {

    // the unique ID of this rule
    private String _id;

    // the name of this rule
    private String _name;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    public DataGeneratorRule(String id, String name) {
        _id = id;
        _name = name;
    }

    /**
     * Returns the rule's unique identifier.
     */
    public String getId() {
        return _id;
    }

    /**
     * Returns the rule's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Execute the logic of the rule on the provided record.
     */
    public abstract void execute(Map<String, String> record, Map<String, Object> options);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataGeneratorRule)) return false;

        DataGeneratorRule that = (DataGeneratorRule)o;

        return _id.equals(that._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
