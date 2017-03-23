package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class TumorRecordNumberRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-record-number";

    private static final String _CRITERIA = "For each patient, each tumor is given a Tumor Record Number starting at 01, incrementing by 1 for each additional tumor.";

    /**
     * Constructor.
     */
    public TumorRecordNumberRule() {
        super(ID, "Tumor Record Number");
    }

    @Override
    public String getCriteria() {
        return _CRITERIA;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("tumorRecordNumber", String.format("%02d", otherRecords.size() + 1));
    }
}
