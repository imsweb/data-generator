package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributedRandomValueGenerator;

public class RaceRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "race";

    // file to the frequencies
    protected static final String _FREQUENCY_FILE = "frequencies/race.csv";

    // random race value generator
    protected static final DistributedRandomValueGenerator _VALUES = new DistributedRandomValueGenerator(_FREQUENCY_FILE);

    /**
     * Constructor.
     */
    public RaceRule() {
        super(ID, "Race 1-5");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("race1", _VALUES.getRandomValue());
        // if race1 is 99 set race2 - race5 to 99, else set them to 88
        String otherRaceCode = record.get("race1").equals("99") ? "99" : "88";
        record.put("race2", otherRaceCode);
        record.put("race3", otherRaceCode);
        record.put("race4", otherRaceCode);
        record.put("race5", otherRaceCode);
    }
}
