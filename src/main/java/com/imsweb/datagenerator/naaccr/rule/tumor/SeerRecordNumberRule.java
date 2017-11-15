package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class SeerRecordNumberRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "seer-rec-num";

    private static final String _DESCRIPTION = "For each patient, each tumor is given a SEER Record Number starting at 01, incrementing by 1 for each additional tumor.";

    /**
     * Constructor.
     */
    public SeerRecordNumberRule() {
        super(ID, "SEER Record Number");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("seerRecordNumber", String.format("%02d", otherRecords.size() + 1));
    }
}
