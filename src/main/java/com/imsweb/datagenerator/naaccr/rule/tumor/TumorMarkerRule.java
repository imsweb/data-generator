package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorTumorRule;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class TumorMarkerRule extends NaaccrDataGeneratorTumorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-marker";

    /**
     * Constructor.
     */
    public TumorMarkerRule() {
        super(ID, "Tumor Marker 1, 2 and 3");
    }

    @Override
    public List<String> getRequiredProperties() {
        return Collections.singletonList("dateOfDiagnosisYear");
    }

    @Override
    public void execute(Tumor tumor, Patient patient, NaaccrDataGeneratorOptions options, Map<String, Object> context) {
        // fields were only used up to 2003
        if (inDxYearRange(tumor, 0, 2003)) {
            setValue(tumor, "tumorMarker1", "9");
            setValue(tumor, "tumorMarker2", "9");
            setValue(tumor, "tumorMarker3", "9");
        }
    }

}
