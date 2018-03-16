package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

public class SexRule extends NaaccrDataGeneratorRule {

    // unique identifier for this rule
    public static final String ID = "sex";

    /**
     * Constructor.
     */
    public SexRule() {
        super(ID, "Sex");
    }

    @Override
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, String> context) {

        String wantedSex = null;
        if (context != null)
            if (propertyHasValue(context, "sex"))
                wantedSex = context.get("sex");

        if (wantedSex == null)
            wantedSex = DistributionUtils.getSex();

        record.put("sex", wantedSex);
    }
}
