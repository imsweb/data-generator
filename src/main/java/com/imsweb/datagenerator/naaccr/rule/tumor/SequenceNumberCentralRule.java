package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class SequenceNumberCentralRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "sequence-number-central";

    /**
     * Constructor.
     */
    public SequenceNumberCentralRule() {
        super(ID, "Sequence Number Central");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {
        if (otherRecords.isEmpty())
            // if this is the only tumor, set sequence number to 00
            record.put("sequenceNumberCentral", "00");
        else {
            // if this is not the only tumor, reorder previously generated tumors 01, 02, 03, ...
            record.put("sequenceNumberCentral", String.format("%02d", otherRecords.size() + 1));
            for (int i = 0; i < otherRecords.size(); i++)
                otherRecords.get(i).put("sequenceNumberCentral", String.format("%02d", i + 1));
        }
    }
}
