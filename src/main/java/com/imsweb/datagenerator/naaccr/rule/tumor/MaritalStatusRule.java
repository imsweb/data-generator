package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.RandomUtils;

public class MaritalStatusRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "marital-status";

    /**
     * Constructor.
     */
    public MaritalStatusRule() {
        super(ID, "Marital Status at DX");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {
        // no maiden name (possibly male) - all possible marital status values are valid (1-6)
        if (!propertyHasValue(record, "nameMaiden"))
            record.put("maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(6) + 1));
        else // patient has maiden name - status may be 2-married, 3-separated, 4-divorced, 5-widowed
            record.put("maritalStatusAtDx", Integer.toString(RandomUtils.nextInt(4) + 2));
    }
}
