package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class CensusRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "census";

    /**
     * Constructor.
     */
    public CensusRule() {
        super(ID, "Census fields");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        setValue(tumor, "censusTract708090", "");
        setValue(tumor, "censusTract2000", "999999");
        setValue(tumor, "censusTract2010", "999999");

        setValue(tumor, "censusTrCert19708090", "");
        setValue(tumor, "censusTrCertainty2000", "9");
        setValue(tumor, "censusTrCertainty2010", "9");

        setValue(tumor, "censusTrPovertyIndictr", "9");
    }
}
