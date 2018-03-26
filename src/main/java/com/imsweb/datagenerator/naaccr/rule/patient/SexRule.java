package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.List;
import java.util.Map;

import com.imsweb.datagenerator.naaccr.NaaccrDataGenerator;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorRule;
import com.imsweb.datagenerator.utils.DistributionUtils;

import static com.imsweb.datagenerator.naaccr.NaaccrDataGenerator.CONTEXT_FLAG_SEX;

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
    public void execute(Map<String, String> record, List<Map<String, String>> otherRecords, NaaccrDataGeneratorOptions options, Map<String, Object> context) {

        record.put("sex", (String)context.getOrDefault(CONTEXT_FLAG_SEX, DistributionUtils.getSex()));

    }
}
