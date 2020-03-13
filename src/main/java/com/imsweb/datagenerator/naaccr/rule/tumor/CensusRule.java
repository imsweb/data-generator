package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class CensusRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "census";

    /**
     * Constructor.
     */
    public CensusRule() {
        super(ID, "Census fields");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        record.put("censusTract708090", "");
        record.put("censusTract2000", "999999");
        record.put("censusTract2010", "999999");

        record.put("censusTrCert19708090", "");
        record.put("censusTrCertainty2000", "9");
        record.put("censusTrCertainty2010", "9");

        record.put("censusTrPovertyIndictr", "9");
    }
}
