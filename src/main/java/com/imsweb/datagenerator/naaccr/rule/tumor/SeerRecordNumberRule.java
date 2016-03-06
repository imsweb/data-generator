package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class SeerRecordNumberRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "seer-rec-num";

    /**
     * Constructor.
     */
    public SeerRecordNumberRule() {
        super(ID, "SEER Record Number");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("seerRecordNumber", String.format("%02d", otherRecords.size() + 1));
    }
}
