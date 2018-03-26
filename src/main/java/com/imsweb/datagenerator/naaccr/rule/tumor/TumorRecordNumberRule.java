package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class TumorRecordNumberRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-record-number";

    /**
     * Constructor.
     */
    public TumorRecordNumberRule() {
        super(ID, "Tumor Record Number");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        record.put("tumorRecordNumber", String.format("%02d", otherRecords.size() + 1));
    }
}
