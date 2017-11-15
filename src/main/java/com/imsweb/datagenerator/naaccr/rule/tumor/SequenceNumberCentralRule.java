package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class SequenceNumberCentralRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "sequence-number-central";

    private static final String _DESCRIPTION = "If a patient has only 1 tumor, it will be given a Sequence Number of 00.<br/>"
            + "If a patient has multiple tumors, each tumor will be given a sequence number starting at 01, incrementing by 1 for each additional tumor.";

    /**
     * Constructor.
     */
    public SequenceNumberCentralRule() {
        super(ID, "Sequence Number Central");
    }

    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
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
