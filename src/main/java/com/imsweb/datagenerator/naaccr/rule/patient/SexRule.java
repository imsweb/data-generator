package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

public class SexRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "sex";

    private static final String _CRITERIA = "Sex is randomly generated based on frequency";

    /**
     * Constructor.
     */
    public SexRule() {
        super(ID, "Sex");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("sex", DistributionUtils.getSex());
    }
}
