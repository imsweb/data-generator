package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class SequenceNumberCentralRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "sequence-number-central";

    /**
     * Constructor.
     */
    public SequenceNumberCentralRule() {
        super(ID, "Sequence Number Central");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        if (patient.getTumors().isEmpty())
            // if this is the only tumor, set sequence number to 00
            setValue(tumor, "sequenceNumberCentral", "00");
        else {
            // if this is not the only tumor, reorder previously generated tumors 01, 02, 03, ...
            setValue(tumor, "sequenceNumberCentral", String.format("%02d", patient.getTumors().size() + 1));
            for (int i = 0; i < patient.getTumors().size(); i++)
                setValue(patient.getTumor(i), "sequenceNumberCentral", String.format("%02d", i + 1));
        }
    }
}
