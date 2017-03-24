package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class IhsRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "ihs";

    private static final String _CRITERIA = "IHS is always left blank.";

    /**
     * Constructor.
     */
    public IhsRule() {
        super(ID, "IHS");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("ihs", "");
    }
}
