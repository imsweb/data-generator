package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;
import com.imsweb.datagenerator.utils.RandomUtils;

public class BirthRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "birth";

    // random birth state value generator
    private DistributedRandomValueGenerator _stateValues;

    /**
     * Constructor.
     */
    public BirthRule() {
        super(ID, "Date of Birth, Birthplace Country and State");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {

        // lazy initialization of random value generator for birth state - this must be done here because we need the state defined in options
        if (_stateValues == null)
            _stateValues = getBirthStateGenerator(options);

        // birth date should be no later than five years prior to min dx date (or current date if min dx date not defined)
        java.time.LocalDate maxBirthDate = options == null ? java.time.LocalDate.now().minusYears(15) : options.getMinDxDate().minusYears(5);
        // limit age to max 100 years
        java.time.LocalDate minBirthDate = maxBirthDate.minusYears(100);

        java.time.LocalDate randomDate = RandomUtils.getRandomDateBetween(minBirthDate, maxBirthDate);

        record.put("birthDateYear", Integer.toString(randomDate.getYear()));
        record.put("birthDateMonth", Integer.toString(randomDate.getMonthValue()));
        record.put("birthDateDay", Integer.toString(randomDate.getDayOfMonth()));
        record.put("birthplaceCountry", "USA");
        record.put("birthplaceState", _stateValues.getRandomValue());
    }

    /**
     * There is a 90% chance that the birth state will be the registry state (if defined); the remaining 10% is divided equally among the remaining states.
     * If there is no registry state defined in the options, the probability will be equal for all states.
     */
    private DistributedRandomValueGenerator getBirthStateGenerator(NaaccrDataGeneratorOptions options) {
        DistributedRandomValueGenerator generator = new DistributedRandomValueGenerator();

        Set<String> states = new HashSet<>(
                Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
                        "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"));

        double pSum = 1.0;
        // if DX state is defined, set probability for that state to 90% and remove it from the list of states to be divided
        if (options != null && options.getState() != null && states.contains(options.getState().toUpperCase())) {
            generator.add(options.getState().toUpperCase(), 0.9);
            states.remove(options.getState().toUpperCase());
            pSum -= 0.9;
        }

        // divide probability of states by the remaining sum
        for (String state : states)
            generator.add(state, 1.0 / states.size() * pSum);

        return generator;
    }
}
