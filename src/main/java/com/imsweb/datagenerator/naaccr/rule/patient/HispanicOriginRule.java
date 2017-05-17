package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

public class HispanicOriginRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "spanish-hispanic-origin";

    /**
     * Constructor.
     */
    public HispanicOriginRule() {
        super(ID, "Spanish/Hispanic Origin");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("spanishHispanicOrigin", DistributionUtils.getHispanicOrigin());
    }
}
