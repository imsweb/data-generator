package com.imsweb.datagenerator.naaccr.rule;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;

public abstract class FrequencyRule extends NaaccrDataGeneratorRule {

    // random field value generator
    private final DistributedRandomValueGenerator _generator;

    /**
     * Constructor
     * @param id rule ID
     * @param name rule name
     * @param path location of frequency data
     */
    protected FrequencyRule(String id, String name, String path) {
        super(id, name);
        _generator = new DistributedRandomValueGenerator(path);
    }

    /**
     * Constructor
     * @param id rule ID
     * @param name rule name
     * @param generator a pre-populated generator
     */
    protected FrequencyRule(String id, String name, DistributedRandomValueGenerator generator) {
        super(id, name);
        _generator = generator;
    }

    /**
     * Return a random value
     * @return a random value from the distributed random value generator
     */
    protected String getRandomValue() {
        return _generator.getRandomValue();
    }

}
