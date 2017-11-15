package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;

public class TumorMarkerRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "tumor-marker";

    private static final String _DESCRIPTION = "If the Date of Diagnosis was in 2003 or earlier, Tumor Marker 1, 2, and 3 are each set to 9. <br/>"
            + "If the Date of Diagnosis was after 2003, these fields are not set.";

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
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        // fields were only used up to 2003
        if (inDxYearRange(record, 0, 2003)) {
            record.put("tumorMarker1", "9");
            record.put("tumorMarker2", "9");
            record.put("tumorMarker3", "9");
        }
    }

}
