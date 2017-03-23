package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class CensusRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "census";

    private static final String _CRITERIA = "Census Tract 1970, 1980, and 1990 are always set to blank. Census Tract 2000 and 2010 are always set to 999999.<br/>"
            + "Census Certainty 1970, 1980, and 1990 are always set to blank. Census Certainty 2000 and 2010 are always set to 9.<br/>"
            + "Census Poverty Indicator is always set to 9.";

    /**
     * Constructor.
     */
    public CensusRule() {
        super(ID, "Census fields");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("censusTract708090", "");
        record.put("censusTract2000", "999999");
        record.put("censusTract2010", "999999");

        record.put("censusCertainty708090", "");
        record.put("censusCertainty2000", "9");
        record.put("censusCertainty2010", "9");

        record.put("censusPovertyIndictr", "9");
    }
}
