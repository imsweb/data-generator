package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

public class RaceRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "race";

    private static final String _DESCRIPTION = "Race 1 is randomly generated based on SEER frequency. If Race 1 is set to 99, Race 2-5 are set to 99 also.<br/>"
            + "Otherwise, Race 2-5 are set to 88.";

    /**
     * Constructor.
     */
    public RaceRule() {
        super(ID, "Race 1-5");
    }


    @Override
    public String getDescription() {
        return _DESCRIPTION;
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options) {
        record.put("race1", DistributionUtils.getRace());
        // if race1 is 99 set race2 - race5 to 99, else set them to 88
        String otherRaceCode = record.get("race1").equals("99") ? "99" : "88";
        record.put("race2", otherRaceCode);
        record.put("race3", otherRaceCode);
        record.put("race4", otherRaceCode);
        record.put("race5", otherRaceCode);
    }
}
