package com.imsweb.datagenerator.record;

import java.util.Map;

import com.imsweb.datagenerator.DataGeneratorRule;

public abstract class RecordDataGeneratorRule implements DataGeneratorRule {

    // the unique ID of this rule
    private final String _id;

    // the name of this rule
    private final String _name;

    /**
     * Constructor.
     * @param id rule ID
     * @param name rule name
     */
    protected RecordDataGeneratorRule(String id, String name) {
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
    public abstract void execute(Map<String, String> rec, Map<String, Object> options);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordDataGeneratorRule)) return false;

        RecordDataGeneratorRule that = (RecordDataGeneratorRule)o;

        return _id.equals(that._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
