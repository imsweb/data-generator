package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.random.RandomUtils;

public class MaritalStatusRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "marital-status";

    private static final String _CRITERIA = "Martial Status at DX is randomly generated based on the patient's Maiden Name.<br/>"
            + "If the patient has no Maiden Name, then a Marital Status between 1 and 6 will be randomly assigned.</br>"
            + "If the patient has a Maiden Name, then a Martial Status between 2 and 5 will be randomly assigned.";

    /**
     * Constructor.
     */
    public MaritalStatusRule() {
        super(ID, "Marital Status at DX");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        // no maiden name (possibly male) - all possible marital status values are valid (1-6)
        if (!propertyHasValue(record, "nameMaiden"))
            record.put("maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(6) + 1));
        else // patient has maiden name - status may be 2-married, 3-separated, 4-divorced, 5-widowed
            record.put("maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(4) + 2));
    }
}
